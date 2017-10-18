package com.wellographics.petbattle.Objects;

import android.content.Context;

import com.wellographics.petbattle.Objects.Battle.Character;
import com.wellographics.petbattle.Objects.Battle.CombatPet;
import com.wellographics.petbattle.Objects.Stuff.PetType;

import java.util.Arrays;

public class Pet {

    public static final int MAX_SPELLS = 6;

    public static final int RARITY_USUAL = 0, RARITY_UNUSUAL = 1, RARITY_RARE = 2, RARITY_EPIC = 3,
            RARITY_LEGENDARY = 4;

    public static final float RARITY_COEFFICIENT = 0.125f;

    protected String pName;

    protected int pPrimalHealth, pPrimalStrength, pPrimalSpeed, pIdentifier, pKills, pSpells[],
            pActualSpells[], pPreviewDrawable, pRarity = RARITY_USUAL;

    protected PetType pType;

    protected Context pContext;

    public Pet(Context context, int id) {
        pContext = context;
        pIdentifier = id;
        pSpells = new int[MAX_SPELLS];
        pActualSpells = new int[CombatPet.MAX_SPELLS_IN_THE_BATTLE];
        setPrimalStats();
    }

    public CombatPet toCombatPet(Character owner) {
        return new CombatPet(pContext, pIdentifier, owner);
    }

    private int[] getStatsArray() {
        final String PATH = "PetStats_" + String.valueOf(pIdentifier);
        int arrayId = pContext.getResources().getIdentifier(PATH, "array", pContext.getPackageName());
        return pContext.getResources().getIntArray(arrayId);
    }

    private String getPetNameFromDataBase() {
        final String PATH = "PetName_" + String.valueOf(pIdentifier);
        int stringId = pContext.getResources().getIdentifier(PATH, "string", pContext.getPackageName());
        return pContext.getResources().getString(stringId);
    }

    private int getPreviewDrawableFromDataBase() {
        final String PATH = "pet_preview_" + String.valueOf(pIdentifier);
        int drawableId = pContext.getResources().getIdentifier(PATH, "drawable", pContext.getPackageName());
        return drawableId;
    }

    private void setPrimalStats() throws NullPointerException {
        int stats[] = getStatsArray();

        pName = getPetNameFromDataBase();
        pPreviewDrawable = getPreviewDrawableFromDataBase();
        pType = new PetType(stats[0]);
        pPrimalHealth = stats[1];
        pPrimalStrength = stats[2];
        pPrimalSpeed = stats[3];
        System.arraycopy(stats, (stats.length - MAX_SPELLS), pSpells, 0, MAX_SPELLS); //Если вышло за пределы, то у второго аргумента (-1)
        //Это вроде бы то же самое, что и сверху
        // for (int i = (stats.length - MAX_SPELLS); i < stats.length; i++) pSpells[i] = stats[i];
    }

    public int findSpellIndexByIdentifier(int identifier) {
        return Arrays.binarySearch(getActualSpellsIdentifierList(), identifier);
    }

    public int getPrimalHealth() {
        return pPrimalHealth;
    }

    public int getPrimalStrength() {
        return pPrimalStrength;
    }

    public int getPrimalSpeed() {
        return pPrimalSpeed;
    }

    public PetType getType() {
        return pType;
    }

    public int getIdentifier() {
        return pIdentifier;
    }

    public int getNumberOfKills() {
        return pKills;
    }

    public int[] getSpellsIdentifiersList() {
        return pSpells;
    }

    public int[] getActualSpellsIdentifierList() {
        return pActualSpells;
    }

    public String getName() {
        return pName;
    }

    public int getPreviewDrawable() {
        return pPreviewDrawable;
    }

    public void setNumberOfKills(int value) {
        pKills = value;
    }

    public void setActualSpells(int[] value) {
        pActualSpells = value;
    }

    public int getRarity() {
        return pRarity;
    }

    public void setRarity(int value) {
        pRarity = value;
    }
}
