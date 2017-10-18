package com.wellographics.petbattle.Bases;

import com.wellographics.petbattle.Logic.BattleActionManager;
import com.wellographics.petbattle.Objects.Battle.Bundles.BattleActionResult;
import com.wellographics.petbattle.Objects.Battle.Character;
import com.wellographics.petbattle.Objects.Battle.CombatPet;
import com.wellographics.petbattle.Objects.Battle.Effect;
import com.wellographics.petbattle.Objects.Battle.Location;
import com.wellographics.petbattle.Objects.Battle.Spell;

public class SpellBase extends EffectBase {

    protected static BattleActionResult justAttack(CombatPet striker, CombatPet target, Spell spell, Location location) {
        return BattleActionManager.dealAttackByPet(striker, target, spell, location);
    }

    /**
     * Укус
     * Наносит @at урона.
     */
    protected static BattleActionResult sp0(CombatPet striker, CombatPet target, Spell spell, Location location) {
        return justAttack(striker, target, spell, location);
    }

    /**
     * Луч
     * Наносит @at урона.
     */
    protected static BattleActionResult sp1(CombatPet striker, CombatPet target, Spell spell, Location location) {
        return justAttack(striker, target, spell, location);
    }

    /**
     * Антимагия
     * Удаляет с игрока все отрицательные и положительные эффекты.
     */
    protected static void sp2(Character target) {
        target.clearEffects();
    }

    /**
     * Страж души
     * Вас окружает стена могущественной магии, которая блокирует следующую атаку.
     */
    protected static void sp3(Character target, Spell spell) {
        Effect effect = spell.getEffect();
        if (spell.getExtraField(0) < spell.getExtraField(1)) {
            effect.setDamageCoefficient(0);
            spell.increaseExtraField(0, 1);
            target.addEffect(effect);
        } else target.removeEffect(effect.getIdentifier());

    }

    /**
     * Чародейская вспышка
     * Концентрирует мощь тайной магии на противнике, нанося @at.
     * Урон возрастает на @e0 после каждого использования. Урон суммируется не более @ed раз подряд.
     */
    protected static BattleActionResult sp4(CombatPet striker, CombatPet target, Spell spell, Location location) {
        Effect effect = spell.getEffect();
        int damage = (int) (1 + BattleActionManager.LEVEL_COEFF_DMG) * spell.getExtraField(0);
        if (effect.getBonusDamage() < damage * spell.getExtraField(1))
            effect.increaseBonusDamage(damage);
        return justAttack(striker, target, spell, location);
    }

    /**
     * Дополнительная броня
     * Дополнительные бронепластины, которые снижают получаемый урон на @e0% на @d.
     */
    protected static void sp5(Character target, Spell spell) {
        Effect effect = spell.getEffect();
        effect.setDamageCoefficient(spell.getExtraField(0) / 100);
        target.addEffect(effect);
    }

    /**
     * Ликвидация
     * Первое использование: увеличивает наносимый урон на 10%.
     * Второе использование: обрушивает на врага шквал ударов и наносит @at.
     */
    protected static BattleActionResult sp6(CombatPet striker, CombatPet defender, Spell spell, Location location) {
        if (spell.getExtraField(2) == 0) {
            Effect effect = spell.getEffect();
            effect.setDamageCoefficient(1 + spell.getExtraField(0) / 100);
            spell.setExtraField(2, 1);
            striker.getOwner().addEffect(effect);
            return null;
        }
        return justAttack(striker, defender, spell, location);
    }
}
