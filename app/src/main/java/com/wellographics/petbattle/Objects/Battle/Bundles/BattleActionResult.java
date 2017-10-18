package com.wellographics.petbattle.Objects.Battle.Bundles;

public class BattleActionResult {

    public static final double CRIT_COEFF = 2, WEAK_COEFF = 0.6, STRONG_COEFF = 1.5;

    private int sDamage = 0, sHeal = 0;

    private boolean sWeak = false, sStrong = false, sMissed = false, sCritical = false;

    public void setWeak(boolean value) {
        sWeak = value;
    }

    public void setStrong(boolean value) {
        sStrong = value;
    }

    public void setMiss(boolean value) {
        sMissed = value;
    }

    public void setCritical(boolean value) {
        sCritical = value;
    }

    public void setDamage(int value) {
        sDamage = value;
    }

    public void setHeal(int value) {
        sHeal = value;
    }

    public boolean isMissed() {
        return sMissed;
    }

    public boolean isCritical() {
        return sCritical;
    }

    public boolean isWeak() {
        return sWeak;
    }

    public boolean isStrong() {
        return sStrong;
    }

    private int getDamage() {
        return sDamage;
    }

    public int getFinallyDamage() {
        double damage = getDamage();

        if (isCritical()) damage *= CRIT_COEFF;

        if (isWeak()) damage *= WEAK_COEFF;
        else if (isStrong()) damage *= STRONG_COEFF;

        return (int) damage;
    }

    public int getHeal() {
        return sHeal;
    }

}