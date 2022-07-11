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
        levelExp.put(5,5);
        levelExp.put(6,5);
        levelExp.put(7,5);
        levelExp.put(8,5);
        levelExp.put(9,5);
        levelExp.put(10,10);
        levelExp.put(11,10);
        levelExp.put(12,10);
        levelExp.put(13,10);
        levelExp.put(14,10);
        levelExp.put(15,15);
        levelExp.put(16,15);
        levelExp.put(17,15);
        levelExp.put(18,15);
        levelExp.put(19,15);
        levelExp.put(20,20);
        levelExp.put(21,20);
        levelExp.put(22,20);
        levelExp.put(23,20);
        levelExp.put(24,20);
        levelExp.put(25,25);
        levelExp.put(26,25);
        levelExp.put(27,25);
        levelExp.put(28,25);
        levelExp.put(29,25);
        levelExp.put(30,25);
        return levelExp;
    }

    public HashMap<Step, Integer> getStepLevel() {
        HashMap<Step, Integer> stepLevel = new HashMap<>();
        stepLevel.put(Step.FIRST, 10);
        stepLevel.put(Step.SECOND, 20);
        return stepLevel;
    }
}