package com.wellographics.petbattle.Managers;

import android.util.Log;

import com.wellographics.petbattle.Bases.SpellBase;
import com.wellographics.petbattle.Objects.Battle.Bundles.BattleData;
import com.wellographics.petbattle.Objects.Battle.Character;
import com.wellographics.petbattle.Objects.Battle.Location;
import com.wellographics.petbattle.Objects.Battle.Spell;
import com.wellographics.petbattle.Objects.Battle.Bundles.BattleActionResult;

import java.util.Random;

public class SpellManager extends SpellBase {

    private Random sRandom;

    private Location sLocation;

    public SpellManager(Location location) {
        sLocation = location;
        sRandom = new Random();
    }

    public BattleActionResult useSpell(BattleData battleData) {
        Spell spell = battleData.getSpell();

        switch (spell.getIdentifier()) {
            case 0:
                return sp0(battleData.getActualPet(true), battleData.getActualPet(false), spell, sLocation);
            case 1:
                return sp1(battleData.getActualPet(true), battleData.getActualPet(false), spell, sLocation);
            case 2:
                sp2(battleData.getDefender());
                return null;
            case 3:
                sp3(battleData.getDefender(), spell);
                return null;
            case 4:
                return sp4(battleData.getActualPet(true), battleData.getActualPet(false), spell, sLocation);
            case 5:
                sp5(battleData.getStriker(), spell);
                return null;
            case 6:
                return sp6(battleData.getActualPet(true), battleData.getActualPet(false), spell, sLocation);
            default:
                Log.wtf("TERRIBLE ERROR", "Invalid spell key: " + String.valueOf(spell.getIdentifier()));
                return null;
        }
    }


}
