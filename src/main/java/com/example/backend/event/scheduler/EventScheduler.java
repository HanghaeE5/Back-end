package com.example.backend.event.scheduler;

import com.example.backend.event.domain.Event;
import com.example.backend.event.domain.Product;
import com.example.backend.event.domain.Stamp;
import com.example.backend.event.domain.StampDate;
import com.example.backend.event.repository.EventRepository;
import com.example.backend.event.repository.ProductRepository;
import com.example.backend.event.repository.StampDateRepository;
import com.example.backend.event.repository.StampRepository;
import com.example.backend.todo.domain.Todo;
import com.example.backend.todo.repository.TodoRepository;
import com.example.backend.user.domain.User;
import com.example.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class EventScheduler {

    private final UserRepository userRepository;
    private final TodoRepository todoRepository;
    private final StampRepository stampRepository;
    private final StampDateRepository stampDateRepository;
    private final EventRepository eventRepository;
    private final ProductRepository productRepository;

    //초, 분, 시, 일, 월, 요
    //요일에서 0과 7은 일요일이며, 1부터 월요일이고 6이 토요일이다.
//    매일 00:05:00, 03:05:00 마다 반복
//    @Scheduled(cron = "0 5 0,3 * * ?")
//    10초마다 테스트용
//    @Scheduled(cron = "*/10 * * * * ?")

    @Scheduled(cron = "0 5 0,3 * * ?")
    @Transactional
    public void eventJob() throws ParseException {

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        log.info("Event Job Start : " + sdf1.format(new Date()));

        String today = sdf2.format(new Date())+" 00:03:00";
        Date todayDateType = sdf1.parse(today);

        //제품 정보 미리 넣어놓기
        List<Product> products = productRepository.findAll();
        if(products.size() <= 0){
            addProduct();
        }

        //어제 날짜 구하기
        Calendar cal = Calendar.getInstance();
        cal.add(cal.DATE, -1); //날짜를 하루 뺀다.
        String yesterday = sdf2.format(cal.getTime());
        Date yesterdayTypeDate = sdf2.parse(yesterday);
        log.info("yesterday : " + yesterday);

        if(!eventRepository.findByRunTime(yesterdayTypeDate).isPresent()){
            List<User> users = userRepository.findAll();
            log.info("회원 수 : " + users.size());
            for (User user: users) {
                List<Todo> todoList = todoRepository.findByUserAndTodoDate(user, yesterdayTypeDate);
                //어제 해야할 Todo 가 없을 때 다른 User로 넘어감
                if (todoList.size() == 0)
                    continue;
                //어제 해야 할 Todo 검사
                boolean checkTodoComplete = true;
                for (Todo todo : todoList){
                    //완료 안된 할 일이 있을 때
                    if (!todo.isState()){
                        checkTodoComplete = false;
                        break;
                    }
                    //완료한 목록이 기준 날짜 보다 크면 ( 어제 다 완료를 못한 거임 )
                    if (todo.getCompleteDate().compareTo(todayDateType) > 0){
                        checkTodoComplete = false;
                        break;
                    }
                }
                //어제 해야할 Todo 를 모두 완료 했을 때
                if(checkTodoComplete){
                    //도장 부여 하기
                    Optional<Stamp> optionalStamp = stampRepository.findByUser(user);
                    Stamp stamp = optionalStamp.isPresent() ? optionalStamp.get() : stampRepository.save(new Stamp(user));

                    //어제 날짜의 도장이 존재 하지 않는 다면 실행하기
                    if(!stampDateRepository.existsByStampAndStampDate(stamp, yesterdayTypeDate)){
                        StampDate stampDate = stampDateRepository.save(new StampDate(yesterdayTypeDate, stamp));
                        stamp.addStamp(stampDate);
                    }
                //어제 해야할 Todo 중에 하나라도 완료 안된게 있으면, 패스
                }
            }

            eventRepository.save(new Event(yesterdayTypeDate));
        }else{
            log.info(yesterday + " 해당 날짜는 이미 실행하였습니다.");
        }
    }


    private void addProduct(){
        productRepository.save(
                Product.builder()
                        .name("황금올리브")
                        .percentage("0.5")
                        .price(19800)
                        .build()
        );
        productRepository.save(
                Product.builder()
                        .name("베스킨라빈스 더블 주니어")
                        .percentage("0.75")
                        .price(4420)
                        .build()
        );
        productRepository.save(
                Product.builder()
                        .name("스타벅스 아메리카노")
                        .percentage("0.75")
                        .price(3830)
                        .build()
        );
        productRepository.save(
                Product.builder()
                        .name("비타오백")
                        .percentage("16")
                        .price(840)
                        .build()
        );
        productRepository.save(
                Product.builder()
                        .name("육개장 사발면")
                        .percentage("16")
                        .price(840)
                        .build()
        );
        productRepository.save(
                Product.builder()
                        .name("마이쮸")
                        .percentage("16")
                        .price(740)
                        .build()
        );
        productRepository.save(
                Product.builder()
                        .name("춥팝춥스")
                        .percentage("50")
                        .price(240)
                        .build()
        );
    }
}
