package com.wellographics.petbattle.Objects.Battle.Bundles;

import android.app.Activity;

import com.wellographics.petbattle.Objects.Battle.Character;
import com.wellographics.petbattle.Objects.Battle.CombatPet;
import com.wellographics.petbattle.Objects.Battle.Spell;
import com.wellographics.petbattle.Objects.Battle.Stage;
import com.wellographics.petbattle.Objects.PlayerHero;

public class BattleData {

    private PlayerHero bPlayer;
    private Character bEnemy;
    private Spell bSpell;
    private boolean bFriendly;
    private Activity bActivity;
    private Stage bStage;

    /** КОНКСТРУКТОР ДЛЯ ЗАКЛИНАНИЙ
     * @param player — игрок
     * @param enemy — противник
     * @param spell — заклинание
     * @param usingByPlayer — заклинание использовано игроком
     */
    public BattleData(PlayerHero player, Character enemy, Spell spell, boolean usingByPlayer) {
        bPlayer = player;
        bEnemy = enemy;
        bSpell = spell;
        bFriendly = usingByPlayer;
    }

    public BattleData(PlayerHero player, Character enemy, Stage stage, Activity activity) {
        bPlayer = player;
        bEnemy = enemy;
        bStage = stage;
        bActivity = activity;
    }

    public boolean isUsedByPlayer() {
        return bFriendly;
    }

    public Spell getSpell() {
        return bSpell;
    }

    public Character getStriker() {
        return (isUsedByPlayer() ? bPlayer : bEnemy);
    }

    public Character getDefender() {
        return (isUsedByPlayer() ? bEnemy : bPlayer);
    }

    public CombatPet[] getPetsArray(boolean friendly) {
        return (friendly ? bPlayer.getCombatPets() : bEnemy.getCombatPets());
    }

    public CombatPet getActualPet(boolean strikerOrDefender) {
        return (strikerOrDefender ? getStriker().getCombatPets()[0] : getDefender().getCombatPets()[0]);
    }

    public Character getEnemy() {
        return bEnemy;
    }

    public PlayerHero getPlayer() {
        return bPlayer;
    }

    public Activity getActivity() {
        return bActivity;
    }

    public Stage getStage() {
        return bStage;
    }

}