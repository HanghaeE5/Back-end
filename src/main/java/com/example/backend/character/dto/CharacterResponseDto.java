package com.example.backend.character.dto;

import com.example.backend.character.domain.Characters;
import com.example.backend.character.domain.Step;
import com.example.backend.character.domain.Type;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CharacterResponseDto {

    private Type type;
    private Step step;
    private Integer level;
    private Integer exp;
    private boolean levelUp;
    private boolean stepUp;

    public CharacterResponseDto(Characters characters) {
        this.type = characters.getType();
        this.step = characters.getStep();
        this.level = characters.getLevel();
        this.exp = characters.getExp();
    }

    public CharacterResponseDto(Characters characters, boolean levelUp, boolean stepUp) {
        this.type = characters.getType();
        this.step = characters.getStep();
        this.level = characters.getLevel();
        this.exp = characters.getExp();
        this.levelUp = levelUp;
        this.stepUp = stepUp;
    }
}
