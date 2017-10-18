package com.wellographics.petbattle.Objects.Battle;
//eActive — срабатывает ли эффект каждый ход

import com.wellographics.petbattle.Objects.Battle.Bundles.BattleActionResult;

public class Effect {

    public final static int D_INFINITY = -1;

    public final static int T_ALL = -10;

    private int eIdentifier = -1, ePrimalDuration = 0, eActualDuration = 0, eDealingDamage = 0, eBonusDamage = 0, eHeal = 0,
            eTargetPetIdentifier = T_ALL;

    private float eDamageCoefficient = 1, eHealCoefficient = 1, eAccuracyCoefficient = 1,
            eCriticalCoefficient = 1;

    private Spell eOwnerSpell;

    private boolean eVisible = false, eActive = false, eStunned = false, eLulled = false;

    public Effect(Spell spell) {
        eOwnerSpell = spell;
        eIdentifier = spell.getIdentifier();
        ePrimalDuration = spell.getDuration();
        eActualDuration = ePrimalDuration;
        eDealingDamage = 0;
        eBonusDamage = 0;
        eHeal = 0;
        eActive = spell.isEffectActive();
    }

    public BattleActionResult dealEffect() {
        return null;
    }

    public void setDuration(int value) {
        eActualDuration = value;
    }

    public int getPrimalDuration() {
        return ePrimalDuration;
    }

    public int getActualDuration() {
        return eActualDuration;
    }

    public void increaseActualDuration(int value) {
        eActualDuration += value;
    }

    public void reduceActualDuration() {
        increaseActualDuration(-1);
    }

    public Spell getSpell() {
        return eOwnerSpell;
    }

    public int getDealingDamage() {
        return eDealingDamage;
    }

    public void increaseDealingDamage(int value) {
        eDealingDamage += value;
    }

    public float getDamageCoefficient() {
        return eDamageCoefficient;
    }

    public void setDamageCoefficient(float value) {
        eDamageCoefficient = value;
    }

    public int getBonusDamage() {
        return eBonusDamage;
    }

    public void increaseBonusDamage(int value) {
        eBonusDamage += value;
    }

    public float getCriticalCoefficient() {
        return eCriticalCoefficient;
    }

    public void setCriticalCoefficient(float value) {
        eCriticalCoefficient = value;
    }

    public int getHeal() {
        return eHeal;
    }

    public float getHealingCoefficient() {
        return eHealCoefficient;
    }

    public void setHealingCoefficient(float value) {
        eHealCoefficient = value;
    }

    public void increaseHeal(int value) {
        eHeal += value;
    }

    public float getAccuracyCoefficient() {
        return eAccuracyCoefficient;
    }

    public void setAccuracyCoefficient(float value) {
        eAccuracyCoefficient = value;
    }

    public int getTargetPetIdentifier() {
        return eTargetPetIdentifier;
    }

    public void setTargetPetIdentifier(int value) {
        eTargetPetIdentifier = value;
    }

    public int getIdentifier() {
        return eIdentifier;
    }

    public void setStunned(boolean value) {
        eStunned = value;
    }

    public void setLulled(boolean value) {
        eLulled = value;
    }

    public void setVisible(boolean value) {
        eVisible = value;
    }

    public void setActive(boolean value) {
        eActive = value;
    }

    public boolean isStunned() {
        return eStunned;
    }

    public boolean isLulled() {
        return eLulled;
    }

    public boolean isVisible() {
        return eVisible;
    }

    public boolean isActive() {
        return eActive;
    }
}
