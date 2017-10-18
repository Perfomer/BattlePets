package com.wellographics.petbattle.Logic;

import com.wellographics.petbattle.Objects.Battle.Bundles.BattleData;
import com.wellographics.petbattle.Objects.Battle.Character;
import com.wellographics.petbattle.Objects.Battle.CombatPet;

public class ArtificialIntelligence {

    public static final int
            ACTION_SWIPE = 3, ACTION_PASS = 4, ACTION_SPELL1 = 0, ACTION_SPELL2 = 1, ACTION_SPELL3 = 2;

    private Character aCharacter;
    private CombatPet[] aPets;

    public ArtificialIntelligence(Character character) {
        aCharacter = character;
        aPets = aCharacter.getCombatPets();
    }

    public int whatToDo(BattleData battleData) {
        //пока что заглушка
        return ACTION_SPELL1;
    }

}
