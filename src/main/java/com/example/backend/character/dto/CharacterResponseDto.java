package com.example.backend.character.dto;

import com.example.backend.character.domain.Characters;
import com.example.backend.character.domain.Step;
import com.example.backend.character.domain.Type;
import com.example.backend.msg.MsgEnum;
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
    private String characterUrl;
    private String characterName;

    public CharacterResponseDto(Characters characters) {
        this.type = characters.getType();
        this.step = characters.getStep();
        this.level = characters.getLevel();
        this.exp = characters.getExp();
        this.study = characters.getStudy();
        this.exercise = characters.getExercise();
        this.promise = characters.getPromise();
        this.shopping = characters.getShopping();
        if (characters.getType().equals(Type.거북이)) {
            if (characters.getStep().equals(Step.FIRST)) {
                this.characterUrl = MsgEnum.TURTLE_ONE_URL.getMsg();
                this.characterName = MsgEnum.TURTLE_ONE_NAME.getMsg();
            } else if (characters.getStep().equals(Step.SECOND)) {
                this.characterUrl = MsgEnum.TURTLE_TWO_URL.getMsg();
                this.characterName = MsgEnum.TURTLE_TWO_NAME.getMsg();
            } else {
                this.characterUrl = MsgEnum.TURTLE_THIRD_URL.getMsg();
                this.characterName = MsgEnum.TURTLE_THIRD_NAME.getMsg();
            }
        } else {
            if (characters.getStep().equals(Step.FIRST)) {
                this.characterUrl = MsgEnum.SLOTH_ONE_URL.getMsg();
                this.characterName = MsgEnum.SLOTH_ONE_NAME.getMsg();
            } else if (characters.getStep().equals(Step.SECOND)) {
                this.characterUrl = MsgEnum.SLOTH_TWO_URL.getMsg();
                this.characterName = MsgEnum.SLOTH_TWO_NAME.getMsg();
            } else {
                this.characterUrl = MsgEnum.SLOTH_THIRD_URL.getMsg();
                this.characterName = MsgEnum.SLOTH_THIRD_NAME.getMsg();
            }
        }
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
