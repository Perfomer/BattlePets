package com.wellographics.petbattle.Logic;

import com.wellographics.petbattle.Objects.Battle.Bundles.BattleActionResult;
import com.wellographics.petbattle.Objects.Battle.Character;
import com.wellographics.petbattle.Objects.Battle.Effect;
import com.wellographics.petbattle.Objects.Battle.Location;
import com.wellographics.petbattle.Objects.Battle.CombatPet;
import com.wellographics.petbattle.Objects.Stuff.PetType;
import com.wellographics.petbattle.Objects.Battle.Spell;

import java.util.Random;

public class BattleActionManager {

    public static final float LEVEL_COEFF_DMG = 0.3f;

    public static final int DEFAULT_DMG = 30;

    public static final int DMG_VARIATION_COEFF = 30, CRIT_PROBABILITY = 10;

    /**
     * МЕТОД НАНЕСЕНИЯ УРОНА ПИТОМЦЕМ С УЧЁТОМ:
     * - вероятности промаха:
     * - c учётом бафа/дебафа от эффектов на меткость;
     * - уровня питомца;
     * - урона питомца;
     * - урона заклинания;
     * - бафа от локации;
     * - вероятности нанесения критического удара:
     * - c учётом бафа/дебафа от эффектов на вероятность критического удара;
     * - бафа или дебафа от противоположности классов петов;
     * - разброса урона (DMG_VARIATION_COEFF / 2)%;
     * - бонуса от ээфектов (пол. или отр.)
     * - множителя урона от эффектов (пол.)
     * - особенностей типов питомцев (классовые бафы)
     *
     * @param striker  — объект атакующего питомца
     * @param target   — объект питомца цели атаки
     * @param spell    — объект класса Spell, который описывает, как наносить урон
     * @param location — объект локации
     * @return объект, содержащий информацию о нанесённом уроне.
     */
    public static BattleActionResult dealAttackByPet(CombatPet striker, CombatPet target, Spell spell, Location location) {
        BattleActionResult result = new BattleActionResult();
        Random random = new Random();

        if (random.nextInt(100) > spell.getAccuracy() * striker.getOwner().getEffectsAccuracyCoefficient()) {
            result.setMiss(true);
            return result;
        }

        result = calculateDamage(striker, target, spell, location.getLocationProtagonistType());

        int damage = target.getType().getTypeIdentifier() == PetType.TP_MAGIC &&
                result.getFinallyDamage() > target.getMaxHealth() / 2 ?
                target.getMaxHealth() / 2 :
                result.getFinallyDamage();

        target.increaseHealth(-damage);

        return result;
    }

    /**
     * МЕТОД НАНЕСЕНИЯ УРОНА ЭФФЕКТОМ С УЧЁТОМ:
     * - уровня питомца;
     * - урона эффекта;
     * - множителя урона от эффектов (пол.)
     *
     * @param effect — объект класса Effect, который описывает как наносить урон
     * @param caster — питомец, который наложил эффект
     * @param target — питомец, который подвергается действию эффекта
     *               - возможен случай: caster == target, это нормально
     * @return объект результата боевого взаимодействия.
     */
    public static BattleActionResult dealAttackByEffect(Effect effect,
                                                        CombatPet caster,
                                                        CombatPet target) {

        BattleActionResult result = new BattleActionResult();
        double damageValue =
                (1 + caster.getOwnerLevel() * LEVEL_COEFF_DMG) * effect.getDealingDamage();
        damageValue *= caster.getOwner().getEffectsDealingDamageCoefficient();
        result.setDamage((int) damageValue);
        target.increaseHealth(-(int) damageValue);
        return result;
    }

    private static BattleActionResult calculateDamage(CombatPet striker,
                                                      CombatPet target,
                                                      Spell spell,
                                                      int locationProtagonistTypeIdentifier) {

        BattleActionResult result = new BattleActionResult();
        Character strikerOwner = striker.getOwner();

        double damageValue =
                (1 + (striker.getOwnerLevel() - 1) * LEVEL_COEFF_DMG) * spell.getDamage() +
                        (striker.getCurrentStrength() * spell.getDamage() / ((1 + (striker.getOwnerLevel() - 1) * LEVEL_COEFF_DMG) * DEFAULT_DMG)) * 0.5;

        if (striker.getType().getProtagonistTypeIdentifier() == target.getType().getTypeIdentifier()) result.setStrong(true);

        else if (striker.getType().getAntagonistTypeIdentifier() == target.getType().getTypeIdentifier()) result.setWeak(true);

        if (striker.getType().getTypeIdentifier() == locationProtagonistTypeIdentifier) damageValue += damageValue * Location.PROTAGONIST_COEFF;

        damageValue = (damageValue * (double) (((100 - DMG_VARIATION_COEFF / 2) + (int) (Math.random() * DMG_VARIATION_COEFF))) / 100);

        if (new Random().nextInt(100) < CRIT_PROBABILITY * strikerOwner.getEffectsCriticalDamageCoefficient()) result.setCritical(true);

        damageValue += strikerOwner.getEffectsBonusDamage();
        if (spell.getEffect() != null && !spell.getEffect().isVisible()) damageValue += spell.getEffect().getBonusDamage();

        damageValue *= strikerOwner.getEffectsDealingDamageCoefficient();

        switch (striker.getType().getTypeIdentifier()) {
            case PetType.TP_HUMANOID:
                striker.increaseHealth(striker.getMaxHealth() / 25);
                break;
            case PetType.TP_ANIMAL:
                if (striker.getCurrentHealth() < striker.getMaxHealth() / 2) damageValue *= 1.25;
        }

        result.setDamage((int) damageValue);

        return result;
    }

}
