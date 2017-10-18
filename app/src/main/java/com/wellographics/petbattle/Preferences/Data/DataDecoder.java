package com.wellographics.petbattle.Preferences.Data;

import android.content.Context;

import com.wellographics.petbattle.Objects.*;
import com.wellographics.petbattle.Objects.Battle.Character;
import com.wellographics.petbattle.Objects.Battle.CombatPet;

import java.util.ArrayList;

public class DataDecoder extends DataOperator {

    private final int MISSING_VALUE = -1;

    private int dCoins, dGems, dLastCompletedStage, dActualPets[];
    private Level dLevel;
    private ArrayList<Pet> dPets;

    public DataDecoder(Context context) {
        super(context);
        refreshAll();
    }

    public void refreshAll() {
        dCoins = MISSING_VALUE;
        dGems = MISSING_VALUE;
        dLastCompletedStage = MISSING_VALUE;
        dActualPets = new int[Character.MAX_PETS_IN_THE_BATTLE];
        dPets = null;
        dLevel = null;
    }

    public int getCoins() {
        if (dCoins == MISSING_VALUE) decodeMoney();
        return dCoins;
    }

    public int getGems() {
        if (dGems == MISSING_VALUE) decodeMoney();
        return dGems;
    }

    public Level getLevel() {
        if (dLevel == null) decodeLevel();
        return dLevel;
    }

    public int getLastCompletedStage() {
        if (dLastCompletedStage == MISSING_VALUE) decodeLastCompletedStage();
        return dLastCompletedStage;
    }

    public ArrayList<Pet> getPetsArray() {
        if (dPets == null) decodePets();
        return dPets;
    }

    public int[] getActualPets() {
        if (dPets == null) decodePets();
        return dActualPets;
    }

    private void decodeMoney() {
        int money[] = new int[2];
        char[] moneyCharArray = pDataOperator.getString(STATS_MONEY, "0#0").toCharArray();
        String value = "";
        for (int i = 0, x = 0; (i < moneyCharArray.length) && (x < money.length); i++) {
            if (moneyCharArray[i] != SEPARATOR) value += moneyCharArray[i];
            else {
                money[x] = Integer.parseInt(value);
                value = "";
                x++;
            }
        }
        dCoins = money[0];
        dGems = money[1];
    }

    private void decodeLevel() {
        int level[] = new int[2];
        char[] levelCharArray = pDataOperator.getString(STATS_LEVEL, "1#0").toCharArray();
        String value = "";
        for (int i = 0, x = 0; (i < levelCharArray.length) && (x < level.length); i++) {
            if (levelCharArray[i] != SEPARATOR) value += levelCharArray[i];
            else {
                level[x] = Integer.parseInt(value);
                value = "";
                x++;
            }
        }
        dLevel = new Level(level[0], level[1]);
    }

    private void decodeLastCompletedStage() {
        dLastCompletedStage = Integer.parseInt(pDataOperator.getString(STAGES_COMPLETED, "0"));
    }

    private void decodePets() {
        ArrayList<Pet> petsArray = new ArrayList<>();
        int[] actualPets = new int[Character.MAX_PETS_IN_THE_BATTLE];
        char[] petsCharArray = pDataOperator.getString(PETS_ACTUAL, "1!#0#0#0#0#;0!#0#0#0#0;0!#0#0#0#0#;").toCharArray();

        String identifier = "", kills = "", spell = "";
        int[] spells = new int[CombatPet.MAX_SPELLS_IN_THE_BATTLE];

        for (int iterator = 0, actualPetIndex = 0, dataIndex = 0, spellIndex = 0; iterator < petsCharArray.length; iterator++) {
            switch (petsCharArray[iterator]) {
                case MARKER:
                    actualPets[actualPetIndex] = Integer.parseInt(identifier);
                    actualPetIndex++;
                    continue;
                case SUBSEPARATOR:
                    Pet pet = new Pet(pContext, Integer.parseInt(identifier));
                    pet.setNumberOfKills(Integer.parseInt(kills));
                    pet.setActualSpells(spells);
                    petsArray.add(pet);

                    kills = "";
                    identifier = "";
                    spells = new int[CombatPet.MAX_SPELLS_IN_THE_BATTLE];
                    spellIndex = 0;
                    dataIndex = 0;

                    continue;
                case SEPARATOR:
                    if (dataIndex > 1) {
                        spells[spellIndex] = Integer.parseInt(spell);
                        spell = "";
                        spellIndex++;
                    }
                    dataIndex++;
                    continue;
                default:
                    switch (dataIndex) {
                        case 0:
                            identifier += petsCharArray[iterator];
                            break;
                        case 1:
                            kills += petsCharArray[iterator];
                            break;
                        default:
                            spell += petsCharArray[iterator];
                    }
            }
        }
        dPets = petsArray;
        dActualPets = actualPets;
    }

}
