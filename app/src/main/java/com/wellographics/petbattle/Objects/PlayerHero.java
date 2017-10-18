package com.wellographics.petbattle.Objects;

import android.content.Context;

import com.wellographics.petbattle.Objects.Battle.Character;
import com.wellographics.petbattle.Objects.Battle.CombatPet;
import com.wellographics.petbattle.Preferences.Data.DataDecoder;
import com.wellographics.petbattle.Preferences.Data.DataSaver;

import java.util.ArrayList;

public class PlayerHero extends Character {

    private int pCoins, pGems, pLastCompletedStage, pActualPetsIdentifiers[];
    private ArrayList<Pet> pPets;
    private DataSaver pDataSaver;
    private DataDecoder pDataLoader;

    public PlayerHero(Context context) {
        super(context);
        pDataLoader = new DataDecoder(cContext);
        pDataSaver = new DataSaver(cContext);
        setActualStats();
    }

    public void saveStats() {
        pDataSaver.saveData(this);
    }

    private void setActualStats() {
        pDataLoader.refreshAll();
        cLevel = pDataLoader.getLevel();
        pGems = pDataLoader.getGems();
        pCoins = pDataLoader.getCoins();
        pPets = pDataLoader.getPetsArray();
        pActualPetsIdentifiers = pDataLoader.getActualPets();
        pLastCompletedStage = pDataLoader.getLastCompletedStage();
        findActualPets();
    }

    public int getCoins() {
        return pCoins;
    }

    public int getGems() {
        return pGems;
    }

    public int getLastCompletedStage() {
        return pLastCompletedStage;
    }

    public Context getContext() {
        return cContext;
    }


    public void refreshPetsOwnerVar() {
        for (CombatPet pet : cPets)
            pet.setOwner(this);
    }


    private void findActualPets() {
        Pet[] petsArray = getAllPets();
        ArrayList<CombatPet> actualpets = new ArrayList<>();

        for (Pet pet : petsArray)
           actualpets.add(pet.toCombatPet(this));
//        for (Pet pet : array) {
//            for (int actPet : pActualPetsIdentifiers)
//                if (pet.getIdentifier() == actPet) {
//                    CombatPet compet = new CombatPet(cContext, pet.getIdentifier(), this);
//                    compet.setFriendly(true);
//                    actualpets[actPetsValue++] = compet;
//                }
//            if (actPetsValue == 2) break;
//        }
        cPets = actualpets.toArray(new CombatPet[actualpets.size()]);
        refreshPetsOwnerVar();
        refreshPetsOwnerVar();
    }

    public Pet[] getAllPets() {
        return pPets.toArray(new Pet[pPets.size()]);
    }

    public int[] getActualPets() {
        return pActualPetsIdentifiers;
    }

}
