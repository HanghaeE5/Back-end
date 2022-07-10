package com.example.backend.todo.dto.response;

import com.example.backend.character.domain.Characters;
import com.example.backend.character.dto.CharacterResponseDto;
import com.example.backend.todo.domain.Todo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TodoDoneResponseDto {
    // todo정보 보다는 캐릭터 정보를 내려주는 역할.. 어디 dto지...
    // CharcterResponseDto + 레벨업인지 아닌지 + 스텝업인지 아닌지 ? 유저 정보는 필요한가..
    private CharacterResponseDto characterInfo;
    private boolean levelUp;
    private boolean stepUp;

    public TodoDoneResponseDto(CharacterResponseDto characterResponseDto) {
        this.characterInfo = characterResponseDto;
    }
}
