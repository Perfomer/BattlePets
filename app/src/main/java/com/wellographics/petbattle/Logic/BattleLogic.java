package com.wellographics.petbattle.Logic;

import android.content.Context;

import com.wellographics.petbattle.Activities.BattleActivity;
import com.wellographics.petbattle.Managers.BattleManager;
import com.wellographics.petbattle.Managers.SpellManager;
import com.wellographics.petbattle.Objects.*;
import com.wellographics.petbattle.Objects.Battle.Bundles.BattleActionResult;
import com.wellographics.petbattle.Objects.Battle.Bundles.BattleData;
import com.wellographics.petbattle.Objects.Battle.Bundles.TurnResult;
import com.wellographics.petbattle.Objects.Battle.Character;
import com.wellographics.petbattle.Objects.Battle.CombatPet;
import com.wellographics.petbattle.Objects.Battle.Location;
import com.wellographics.petbattle.Objects.Battle.Spell;
import com.wellographics.petbattle.Objects.Battle.Stage;


public class BattleLogic {

    private BattleActivity bBattleActivity;

    private BattleManager bBManager;

    private SpellManager bSpellManager;

    private Stage bStage;

    private Character bEnemy;

    private Location bLocation;

    private PlayerHero bPlayer;

    private int bTurn = 0, bCatchProbability = 0;

    private Spell bLastUsedSpellByEnemy;

    public BattleLogic(BattleActivity activity, int stageIdentifier, BattleManager battleManager) {
        bBattleActivity = activity;
        bStage = new Stage(activity, stageIdentifier);
        bPlayer = new PlayerHero(activity);
        bEnemy = new Character(activity, bStage);
        bLocation = bStage.getLocation();
        bBManager = battleManager;
        bBManager.setInterfaceData(new BattleData(bPlayer, bEnemy, bStage, bBattleActivity));
        bSpellManager = new SpellManager(bLocation);

        setActualStatsDEBUG(false);
        setActualStatsDEBUG(true);
        bBManager.setBlinkingTurnIndicator(defineAttacker());
    }

    public void startBattle() {

    }

    /** МЕТОД, ОПРЕДЕЛЯЮЩИЙ ТОГО, КТО АТАКУЕТ ПЕРВЫМ
     * - атакует первым тот питомец, чья скорость выше.
     * @return false (enemy), true (player).
     */
    private boolean defineAttacker() {
        return getActualCombatPet(true).getCurrentSpeed() <
                getActualCombatPet(false).getCurrentSpeed();
    }

    private CombatPet getAttacker() {
        return defineAttacker() ?
                bPlayer.getCombatPets()[CombatPet.INDEX_MAIN] : bEnemy.getCombatPets()[CombatPet.INDEX_MAIN];
    }

    private CombatPet getDefender() {
        return defineAttacker() ?
                bEnemy.getCombatPets()[CombatPet.INDEX_MAIN] : bPlayer.getCombatPets()[CombatPet.INDEX_MAIN];
    }

    private BattleActionResult dealActionByEnemy() {
        BattleData data = new BattleData(bPlayer, bEnemy, bStage, bBattleActivity);
        int action = bEnemy.getAI().whatToDo(data);
        switch (action) {
            case ArtificialIntelligence.ACTION_PASS: return null;
            case ArtificialIntelligence.ACTION_SWIPE: return null;
            default:
                bLastUsedSpellByEnemy = new Spell(bBattleActivity, bEnemy.getCombatPets()[CombatPet.INDEX_MAIN].getActualSpellsIdentifierList()[action]);
                return bSpellManager.useSpell(
                        new BattleData(
                                bPlayer,
                                bEnemy,
                                bLastUsedSpellByEnemy,
                                false));
        }
    }

    public void testButtonDEBUG() {
        BattleActionResult result = new BattleActionResult();
        result.setDamage(100);
        result.setCritical(true);
        bBManager.createSwimmingDamage(bPlayer.getCombatPets()[CombatPet.INDEX_MAIN], result, true);
    }

    public BattleActionResult useSpell(int buttonIndex) {
        if (buttonIndex < CombatPet.MAX_SPELLS_IN_THE_BATTLE * 2) {
            Spell spell = new Spell(
                    bBattleActivity.getApplicationContext(),
                    bPlayer.getCombatPets()[0].getSpellId(buttonIndex));

            bBManager.dealTurn(
                    new TurnResult(
                            getAttacker(),
                            getDefender(),
                            spell,
                            bSpellManager.useSpell(new BattleData(bPlayer, bEnemy, spell, true)),
                            dealActionByEnemy(),
                            bLastUsedSpellByEnemy));
        }

        setActualStatsDEBUG(true);
        setActualStatsDEBUG(false);

        return null;
    }


    public CombatPet getActualCombatPet(boolean enemyPet) {
        return enemyPet ? bEnemy.getCombatPets()[CombatPet.INDEX_MAIN] : bPlayer.getCombatPets()[CombatPet.INDEX_MAIN];
    }

    /* МЕТОД, МЕНЯЮЩИЙ МЕСТАМИ ТЕКУЩЕГО ПИТОМЦА И ПИТОМЦА ПО ИНДЕКСУ ИЗ АРГУМЕНТА */
    public void swapPet(boolean byPlayer, int indexOfActualPet) {
        CombatPet[] pets = sortPetsArray(byPlayer ? bPlayer.getCombatPets() : bEnemy.getCombatPets());

        if (pets[indexOfActualPet].isAlive()) {
            CombatPet temp = pets[CombatPet.INDEX_MAIN];
            pets[CombatPet.INDEX_MAIN] = pets[indexOfActualPet];
            pets[indexOfActualPet] = temp;
        }
    }

    /* МЕТОД, КОТОРЫЙ СДВИГАЕТ ВСЕХ МЁРТВЫХ ПИТОМЦЕВ В КОНЕЦ СПИСКА */
    private CombatPet[] sortPetsArray(CombatPet[] pets) {
        for(int i = 0; i > pets.length - 1; i++)
            for(int j = 0; j < i; j++)
                if (!pets[j].isAlive() && pets[j + 1].isAlive()) {
                CombatPet temp = pets[j];
                pets[j] = pets[j + 1];
                pets[j + 1] = temp;
            }
        return pets;
    }

    public void setActualStatsDEBUG(boolean enemy) {
        CombatPet pb = getActualCombatPet(enemy);
        bBattleActivity.getStatsView(enemy).setText(
                "Health: " + String.valueOf(pb.getCurrentHealth()) +
                        "\nSpeed: " + String.valueOf(pb.getCurrentSpeed()) +
                        "\nAttack: " + String.valueOf(pb.getCurrentStrength()));
    }

    public Location getLocation() {
        return bLocation;
    }

    public Context getContext() {
        return bBattleActivity;
    }

    public Character getEnemy() {
        return bEnemy;
    }

    public PlayerHero getPlayerHero() {
        return bPlayer;
    }

    public Stage getStage() {
        return bStage;
    }

}
