package com.example.backend.event.service;

import com.example.backend.event.domain.Product;
import com.example.backend.event.domain.Stamp;
import com.example.backend.event.domain.Winning;
import com.example.backend.event.dto.EventRequestDto;
import com.example.backend.event.dto.EventResponseDto;
import com.example.backend.event.dto.ProductResponseDto;
import com.example.backend.event.repository.ProductRepository;
import com.example.backend.event.repository.StampRepository;
import com.example.backend.event.repository.WinningRepository;
import com.example.backend.exception.CustomException;
import com.example.backend.exception.ErrorCode;
import com.example.backend.msg.MsgEnum;
import com.example.backend.notification.domain.Type;
import com.example.backend.notification.dto.NotificationRequestDto;
import com.example.backend.notification.service.NotificationService;
import com.example.backend.user.domain.User;
import com.example.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventService {

    private final StampRepository stampRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final WinningRepository winningRepository;
    private final NotificationService notificationService;


    public EventResponseDto getEventInfo(String email){
        User user = getUser(email);
        Stamp stamp = getStamp(user);

        return new EventResponseDto(stamp);
    }


    @Transactional
    public EventResponseDto stampToCoupon(String email){
        User user = getUser(email);
        Stamp stamp = getStamp(user);

        if (stamp.getStamp() < 1){
            throw new CustomException(ErrorCode.STAMP_CNT_LESS);
        }

        stamp.stampToCoupon();

        NotificationRequestDto notificationRequestDto = new NotificationRequestDto(
                Type.이벤트,
                "이벤트 쿠폰이 지급되었습니다!"
        );
        notificationService.sendNotification(user.getUserSeq(), notificationRequestDto);

        return new EventResponseDto(stamp);
    }

    @Transactional
    public ProductResponseDto openLuckyBox(String email){
        User user = getUser(email);
        Stamp stamp = getStamp(user);

        if (stamp.getCoupon() <= 0){
            throw new CustomException(ErrorCode.COUPON_NOT_FOUNT);
        }

        // 상자를 열었으니, 쿠폰 1개 사용
        stamp.openBox();

        // 상품 리스트 가져오기
        List<Product> products = productRepository.findAll();

        //랜덤으로 상품 뽑기
        Product randomProduct = randomBox(products);

        //사용자가 뽑은 상품 저장
        Winning winning = winningRepository.save(new Winning(user.getUserSeq(), randomProduct.getId()));


        return new ProductResponseDto(randomProduct, winning.getId());
    }

    @Transactional
    public String eventPhoneRegister(String email, EventRequestDto eventRequestDto){
        User user = getUser(email);
        Winning winning = winningRepository.findById(eventRequestDto.getEventId()).orElseThrow(
                () -> new CustomException(ErrorCode.WINNING_NOT_FOUNT)
        );
        if (user.getUserSeq().equals(winning.getUserId())){
            winning.addPhone(eventRequestDto.getPhone());
        }else{
            throw new CustomException(ErrorCode.WINNING_NOT_FOUNT);
        }
        return MsgEnum.EVENT_PHONE_SUCCESS.getMsg();
    }

    private Product randomBox(List<Product> products){
        Product result = new Product();
        double tmpRandom = (Math.random() * 100);
        double tmpRatePrev = 0, tmpRateNext = 0;
        //소수 둘째자리까지 절삭
        tmpRandom = Math.round(tmpRandom * 100) / 100.0;

        HashMap<String, Object> map = new HashMap<>();
        ArrayList<HashMap<String, Object>> list = new ArrayList<>();
        for (Product p : products){
            map.put("rate",  p.getPercentage());
            map.put("value", p);
            list.add(map);
            map = new HashMap<>();
        }
        for(int i = 0; i < list.size(); i++) {
            if(tmpRandom == 100) {
                //만약 난수가 100이라면 가장 마지막에있는 list 인덱스에 있는 value 적용
                result = products.get(products.size()-1);
                break;
            } else {
                double rate = Double.parseDouble(list.get(i).get("rate").toString());
                tmpRateNext = tmpRatePrev + rate;
                if(tmpRandom >= tmpRatePrev && tmpRandom < tmpRateNext) {
                    result = (Product) list.get(i).get("value");
                    break;
                } else {
                    tmpRatePrev = tmpRateNext;
                }
            }
        }
        log.info(tmpRandom +" : "+ result.getName());
        return result;
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
    }

    private Stamp getStamp(User user) {
        Stamp stamp = stampRepository.findByUser(user).orElseThrow(
                () -> new CustomException(ErrorCode.STAMP_TABLE_NULL)
        );
        return stamp;
    }

}
