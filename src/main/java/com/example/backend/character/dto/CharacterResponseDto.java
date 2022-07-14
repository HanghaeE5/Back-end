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
    private Integer study;
    private Integer exercise;
    private Integer promise;
    private Integer shopping;

    public CharacterResponseDto(Characters characters) {
        this.type = characters.getType();
        this.step = characters.getStep();
        this.level = characters.getLevel();
        this.exp = characters.getExp();
        this.study = characters.getStudy();
        this.exercise = characters.getExercise();
        this.promise = characters.getPromise();
        this.shopping = characters.getShopping();
    }

    public CharacterResponseDto(Characters characters, boolean levelUp, boolean stepUp) {
        this.type = characters.getType();
        this.step = characters.getStep();
        this.level = characters.getLevel();
        this.exp = characters.getExp();
        this.levelUp = levelUp;
        this.stepUp = stepUp;
        this.study = characters.getStudy();
        this.exercise = characters.getExercise();
        this.promise = characters.getPromise();
        this.shopping = characters.getShopping();
    }
}
