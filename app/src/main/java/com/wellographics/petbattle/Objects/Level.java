package com.wellographics.petbattle.Objects;

public class Level {

    private int pLevel, pExperience;

    public Level(int level, int experience) {
        pLevel = level;
        pExperience = experience;
    }

    public int getLevelValue() {
        return pLevel;
    }

    public int getExperienceValue() {
        return pExperience;
    }


}
