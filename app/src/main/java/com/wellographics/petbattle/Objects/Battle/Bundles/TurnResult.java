package com.wellographics.petbattle.Objects.Battle.Bundles;

import com.wellographics.petbattle.Objects.Battle.CombatPet;
import com.wellographics.petbattle.Objects.Battle.Spell;

public class TurnResult {

    private CombatPet tAttacker, tDefender;

    private Spell tAttackerSpell, tDefenderSpell;

    private BattleActionResult tAttackResult, tAnswerResult;

    public TurnResult(CombatPet attacker, CombatPet defender, Spell attack, BattleActionResult attackResult, BattleActionResult answerResult, Spell answer) {

        tAttacker = attacker;
        tDefender = defender;
        tAttackerSpell = attack;
        tDefenderSpell = answer;
        tAttackResult = attackResult;
        tAnswerResult = answerResult;

    }

    public CombatPet getDefender() {
        return tDefender;
    }

    public CombatPet getAttacker() {
        return tAttacker;
    }

    public Spell getStrikerSpell() {
        return tAttackerSpell;
    }

    public Spell getDefenderSpell() {
        return tDefenderSpell;
    }

    public BattleActionResult getActionResult() {
        return tAttackResult;
    }

    public BattleActionResult getAnswerResult() {
        return tAnswerResult;
    }
}
