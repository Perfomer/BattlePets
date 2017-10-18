package com.wellographics.petbattle.Objects.Battle;

import android.content.Context;

import com.wellographics.petbattle.Logic.ArtificialIntelligence;
import com.wellographics.petbattle.Objects.Level;
import com.wellographics.petbattle.Objects.Pet;

import java.util.ArrayList;

public class Character {

    public static final int MAX_PETS_IN_THE_BATTLE = 3;

    protected ArtificialIntelligence cAI;
    protected CombatPet[] cPets;
    protected Context cContext;
    protected Level cLevel;
    protected boolean cFriendly;
    protected ArrayList<Effect> cEffectsList;
    private Effect[] cEffects;
    private Stage cStage;

    protected Character(Context context) {
        cContext = context;
        cFriendly = true;
        cEffectsList = new ArrayList<>();
    }

    public Character(Context context, Stage stage) {
        this(context);
        cFriendly = false;
        cStage = stage;
        setPrimalStats();
    }

    private void setPrimalStats() {
        cLevel = cStage.getOwnersLevel();
        cPets = toCombatPet(cStage.getPetsArray(), this);
        cAI = new ArtificialIntelligence(this);
    }

    private CombatPet[] toCombatPet(Pet[] pets, Character owner) {
        CombatPet compets[] = new CombatPet[pets.length];
        for (int i = 0; i < pets.length; i++) {
            CombatPet pet = new CombatPet(cContext, pets[i].getIdentifier(), owner);
            pet.setActualSpells(pets[i].getActualSpellsIdentifierList());
            compets[i] = pet;
        }
        return compets;
    }

    public int getEffectsDealingDamage() {
        int result = 0;
        for (Effect effect : getEffects())
            result += effect.getDealingDamage();
        return result;
    }

    public int getEffectsBonusDamage() {
        int result = 0;
        for (Effect effect : getEffects())
            result += effect.getBonusDamage();
        return result;
    }

    public float getEffectsDealingDamageCoefficient() {
        float result = 1;
        for (Effect effect : getEffects())
            result *= effect.getDamageCoefficient();
        return result;
    }

    public float getEffectsCriticalDamageCoefficient() {
        float result = 1;
        for (Effect effect : getEffects())
            result *= effect.getCriticalCoefficient();
        return result;
    }

    public float getEffectsAccuracyCoefficient() {
        float result = 1;
        for (Effect effect : getEffects())
            result *= effect.getAccuracyCoefficient();
        return result;
    }

    public void addEffect(Effect effect) {
        int id = effect.getIdentifier();
        boolean presence = false;
        for (Effect eff : cEffectsList) {
            if (eff.getIdentifier() == id) {
                presence = true;
                cEffectsList.set(cEffectsList.indexOf(eff), effect);
                break;
            }
        }
        if (!presence) cEffectsList.add(effect);

        initializeEffectsArray();
    }

    public void clearEffects() {
        cEffectsList.clear();
    }

    public Effect[] getEffects() {
        initializeEffectsArray();
        return cEffects;
    }

    public boolean removeEffect(int identifier) {
        for (Effect effect : cEffectsList)
            if (effect.getIdentifier() == identifier) {
                cEffectsList.remove(effect);
                return true;
            }
        return false;
    }

    private void initializeEffectsArray() {
        cEffects = cEffectsList.toArray(new Effect[cEffectsList.size()]);
    }

    public boolean isFriendly() {
        return cFriendly;
    }


    public CombatPet[] getCombatPets() {
        return cPets;
    }

    public int getIndexOfPet(CombatPet pet) {
        for (int i = 0; i < cPets.length; i++)
            if (pet.getIdentifier() == cPets[i].getIdentifier()) return i;
        return -1;
    }

    public Level getLevel() {
        return cLevel;
    }

    public ArtificialIntelligence getAI() {
        return cAI;
    }


}
