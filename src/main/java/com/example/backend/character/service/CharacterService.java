package com.example.backend.character.service;

import com.example.backend.character.domain.Characters;
import com.example.backend.character.domain.Type;
import com.example.backend.character.repository.CharacterRepository;
import com.example.backend.exception.CustomException;
import com.example.backend.exception.ErrorCode;
import com.example.backend.todo.domain.Todo;
import com.example.backend.todo.dto.response.TodoDoneResponseDto;
import com.example.backend.user.domain.User;
import com.example.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CharacterService {

    private final UserRepository userRepository;
    private final CharacterRepository characterRepository;

    // 캐릭터 선택
    public void selectCharacter(String email, Type type) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        Characters characters = new Characters(user, type);
        characterRepository.save(characters);
    }

    // Todo완료 시 캐릭터 성장
    public TodoDoneResponseDto upgrade(String email, Todo todo) {

    }


    // reload 시에 보내줄 CharacterDto -> 연관관계 매핑 해야됨


    // todoDone 호출 시 경험치 UP / 각 경험치에 따른


}
