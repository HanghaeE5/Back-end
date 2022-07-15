package com.example.backend.character.domain;

import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class CollectionBean {

    public HashMap<Integer, Integer> getLevelExp() {
        HashMap<Integer, Integer> levelExp = new HashMap<>();
        levelExp.put(1,1);
        levelExp.put(2,1);
        levelExp.put(3,1);
        levelExp.put(4,1);
        levelExp.put(5,2);
        levelExp.put(6,2);
        levelExp.put(7,2);
        levelExp.put(8,2);
        levelExp.put(9,2);
        levelExp.put(10,3);
        levelExp.put(11,3);
        levelExp.put(12,3);
        levelExp.put(13,3);
        levelExp.put(14,3);
        levelExp.put(15,5);
        levelExp.put(16,5);
        levelExp.put(17,5);
        levelExp.put(18,5);
        levelExp.put(19,5);
        levelExp.put(20,10);
        levelExp.put(21,10);
        levelExp.put(22,10);
        levelExp.put(23,10);
        levelExp.put(24,10);
        levelExp.put(25,10);
        levelExp.put(26,10);
        levelExp.put(27,10);
        levelExp.put(28,10);
        levelExp.put(29,10);
        levelExp.put(30,10);
        return levelExp;
    }

    public HashMap<Step, Integer> getStepLevel() {
        HashMap<Step, Integer> stepLevel = new HashMap<>();
        stepLevel.put(Step.FIRST, 10);
        stepLevel.put(Step.SECOND, 20);
        return stepLevel;
    }
}