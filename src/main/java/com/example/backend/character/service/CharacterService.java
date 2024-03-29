package com.example.backend.character.service;

import com.example.backend.character.domain.Characters;
import com.example.backend.character.domain.CollectionBean;
import com.example.backend.character.dto.CharacterRequestDto;
import com.example.backend.character.dto.CharacterResponseDto;
import com.example.backend.character.repository.CharacterRepository;
import com.example.backend.exception.CustomException;
import com.example.backend.exception.ErrorCode;
import com.example.backend.notification.domain.Notification;
import com.example.backend.notification.domain.Type;
import com.example.backend.notification.dto.NotificationRequestDto;
import com.example.backend.notification.repository.NotificationRepository;
import com.example.backend.notification.service.NotificationService;
import com.example.backend.todo.domain.Category;
import com.example.backend.todo.domain.Todo;
import com.example.backend.todo.dto.response.TodoDoneResponseDto;
import com.example.backend.todo.repository.TodoRepository;
import com.example.backend.user.domain.User;
import com.example.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CharacterService {

    private final CollectionBean standard;
    private final UserRepository userRepository;
    private final CharacterRepository characterRepository;
    private final TodoRepository todoRepository;
    private final NotificationRepository notificationRepository;


    // 캐릭터 선택
    @Transactional
    public void selectCharacter(String email, CharacterRequestDto requestDto) {

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        Characters characters = new Characters(requestDto, user);
        characterRepository.save(characters);

    }


    // Todo완료 시 캐릭터 성장
    // 경험치 1 증가
    // 완료한 todo를 character 연관관계에 등록? 필요한가...?
    // 경험치가 특정 값 이상이면 레벨업
    // 레벨이 특정 값 이상이면 스텝업
    // ..
    @Transactional
    public TodoDoneResponseDto upgrade(String email, Todo todo) {

        Integer todayDone;
        boolean levelUp = false;
        boolean stepUp = false;
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        Characters characters = characterRepository.findById(user.getUserSeq()).orElseThrow(
                () -> new CustomException(ErrorCode.CHARACTER_NOT_FOUND)
        );
        todayDone = todoRepository.findTodayDone(user).size();
        if (todayDone <= 10) {
            if (todo.getCategory() != Category.ETC) {
                characters.upgradeMedal(todo.getCategory());
            }
            characters.addExp();
            if (Objects.equals(characters.getExp(), standard.getLevelExp().get(characters.getLevel()))) {
                characters.levelUp();
                levelUp = true;
                // 알림
                NotificationRequestDto requestDto = new NotificationRequestDto(
                        Type.캐릭터, "레벨업을 축하드립니다."
                );
                notificationRepository.save(new Notification(requestDto, user));
            }
            if (Objects.equals(characters.getLevel(), standard.getStepLevel().get(characters.getStep()))) {
                characters.stepUp();
                stepUp = true;
                // 알림
                NotificationRequestDto requestDto = new NotificationRequestDto(
                        Type.캐릭터, "스텝업을 축하드립니다."
                );
                notificationRepository.save(new Notification(requestDto, user));
            }
        }
        todo.addCharacter(characters);
        characters.addTodo(todo);
        CharacterResponseDto characterResponseDto = new CharacterResponseDto(characters, levelUp, stepUp, standard.getLevelExp().get(characters.getLevel()), todayDone);
        TodoDoneResponseDto responseDto = new TodoDoneResponseDto(characterResponseDto);

        return responseDto;
    }


    // reload 시에 보내줄 CharacterDto -> 연관관계 매핑 해야됨
    // 유저 정보 조회 Controller 에서 CharacterResponeDto 추가
    public CharacterResponseDto getCharacterInfo(String email) {

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        Characters c = characterRepository.findById(user.getUserSeq()).orElseThrow(
                () -> new CustomException(ErrorCode.CHARACTER_NOT_FOUND)
        );
        CharacterResponseDto responseDto = new CharacterResponseDto(c, standard.getLevelExp().get(c.getLevel()));

        return responseDto;
    }

}
