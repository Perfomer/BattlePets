package com.wellographics.petbattle.Objects.Battle;


import android.content.Context;
import android.content.res.Resources;

import com.wellographics.petbattle.Logic.SpellLogic;
import com.wellographics.petbattle.R;

import java.lang.*;
import java.lang.Character;
import java.util.ArrayList;
import java.util.Arrays;

/** ТИПЫ ЗАКЛИНАНИЙ:
 * 0 — эффект
 * 1 — фильтр эффектов
 * 2 — атака
 * 3 — атака + эффект
 * 4 — лечение
 * 5 — лечение + эффект
 * 6 — лечение + атака
 * 7 — лечение + атака + эффект
 */

public class Spell {

    public static final int TP_EFFECT = 0, TP_FILTER_EFFECT = 1, TP_ATTACK = 2, TP_ATTACK_EFFECT = 3,
        TP_HEAL = 4, TP_HEAL_EFFECT = 5, TP_HEAL_ATTACK = 6, TP_HEAL_ATTACK_EFFECT = 7;

    public static final float RATIO_DMG_INCREASE = 0.33f;

    public static final int MAX_EXTRA_FIELDS = 5;

    private static final String ATTACK_VALUE = "@at", HEAL_VALUE = "@h", COOLDOWN_VALUE = "@c",
        DURATION_VALUE = "@d", ACCURACY_VALUE = "@ac", EXTRA_PREFIX = "@e";

    private int sIdentifier, sSpellType, sAccuracy, sCooldown, sDamage, sHeal, sDuration,
        sExtraFields[];

    private boolean sMovable, sInstant, sActiveEffect;

    private Effect sEffect;

    private Context sContext;

    public Spell(Context context, int identifier) {
        sIdentifier = identifier;
        sContext = context;
        setStats();
        if (sInstant) sEffect = new Effect(this);
    }

    private void setStats() {
        int[] statsArray = getStatsArray(),
            extraStatsArray = getExtraStatsArray();
        sDamage = statsArray[0];
        sHeal = statsArray[1];
        sAccuracy = statsArray[2];
        sDuration = statsArray[3];
        sCooldown = statsArray[4];
        if (extraStatsArray != null) {
            sExtraFields = extraStatsArray;
        }
        sMovable = getMovable();
        sInstant = getInstant();
        sSpellType = defineSpellType();
    }

    public boolean isAttack() {
        return sDamage > 0;
    }

    private int defineSpellType() {
        int type = -1;
        if (sInstant) {
            if (sDuration == 0) type = TP_FILTER_EFFECT;
            else if (sDamage != 0 && sHeal != 0) type = TP_HEAL_ATTACK_EFFECT;
            else if (sDamage != 0) type = TP_ATTACK_EFFECT;
            else if (sHeal != 0) type = TP_HEAL_EFFECT;
            else type = TP_EFFECT;
        }
        else {
            if (sDamage != 0 && sHeal != 0) type = TP_HEAL_ATTACK;
            else if (sDamage != 0) type = TP_ATTACK;
            else if (sHeal != 0) type = TP_HEAL;
        }
        return type;
    }

    /** МЕТОД ПОЛУЧЕНИЯ ТИПА ЗАКЛИНАНИЯ
     * 0 — эффект
     * 1 — фильтр эффектов
     * 2 — атака
     * 3 — атака + эффект
     * 4 — лечение
     * 5 — лечение + эффект
     * 6 — лечение + атака
     * 7 — лечение + атака + эффект
     * @return тип заклинания.
     */
    public int getSpellType() {
        return sSpellType;
    }

    private String parseDescription(String source, int ownerLevel) {
        String result = source;

        result = result.replace(ATTACK_VALUE,
                String.valueOf((int)(1 + RATIO_DMG_INCREASE * ownerLevel) * sDamage) +
                        sContext.getResources().getString(R.string.damage));

        result = result.replace(HEAL_VALUE,
                String.valueOf((int)(1 + RATIO_DMG_INCREASE * ownerLevel) * sHeal) +
                        sContext.getResources().getString(R.string.heal));

        result = result.replace(COOLDOWN_VALUE, String.valueOf(sCooldown));
        result = result.replace(DURATION_VALUE, String.valueOf(sDuration) +
                sContext.getResources().getString(R.string.rounds));
        result = result.replace(ACCURACY_VALUE, String.valueOf(sAccuracy));

        return result;
    }

    private int[] getStatsArray() {
        final String PATH = "SpellStats_" + String.valueOf(sIdentifier);
        int arrayId =
                sContext.getResources().getIdentifier(PATH, "array", sContext.getPackageName());
        return sContext.getResources().getIntArray(arrayId);
    }

    private int[] getExtraStatsArray() {
        try {
            final String PATH = "SpellExtraStats_" + String.valueOf(sIdentifier);
            int arrayId =
                    sContext.getResources().getIdentifier(PATH, "array", sContext.getPackageName());
            return sContext.getResources().getIntArray(arrayId);
        }catch (Resources.NotFoundException e) {
            return  null;
        }
    }

    public int getIconResource() {
        final String PATH = "spell_icon_" + String.valueOf(sIdentifier);
        int resId =
                sContext.getResources().getIdentifier(PATH, "drawable", sContext.getPackageName());
        return resId;
    }

    public String getSpellName() {
        final String PATH = "SpellName_" + String.valueOf(sIdentifier);
        int resId =
                sContext.getResources().getIdentifier(PATH, "string", sContext.getPackageName());
        return sContext.getString(resId);
    }

    private boolean getMovable() {
        final String PATH = "SpellMovable_" + String.valueOf(sIdentifier);
        int resId = sContext.getResources().getIdentifier(PATH, "bool", sContext.getPackageName());
        return sContext.getResources().getBoolean(resId);
    }

    private boolean getInstant() {
        if (sDuration != 0) return true;
        final String PATH = "SpellEffect_" + String.valueOf(sIdentifier);
        int resId = sContext.getResources().getIdentifier(PATH, "bool", sContext.getPackageName());
        return sContext.getResources().getBoolean(resId);
    }

    private boolean getEffectActive() {
        final String PATH = "SpellEffectActive_" + String.valueOf(sIdentifier);
        int resId = sContext.getResources().getIdentifier(PATH, "bool", sContext.getPackageName());
        return sContext.getResources().getBoolean(resId);
    }

    public String getSpellDescription(int ownerLevel) {
        final String PATH = "SpellDescription_" + String.valueOf(sIdentifier);
        int resId =
                sContext.getResources().getIdentifier(PATH, "string", sContext.getPackageName());
        return parseDescription(sContext.getString(resId), ownerLevel);
    }

    public boolean isMovable() {
        return sMovable;
    }

    public boolean isInstant() {
        return sInstant;
    }

    public boolean isEffectActive() {
        return sActiveEffect;
    }

    public int getAccuracy() {
        return sAccuracy;
    }

    public int getCooldown() {
        return sCooldown;
    }

    public int getDamage() {
        return sDamage;
    }

    public int getHeal() {
        return sHeal;
    }

    public int getDuration() {
        return sDuration;
    }

    public int getExtraField(int index) throws IndexOutOfBoundsException {
        return sExtraFields[index];
    }

    public void setExtraField(int index, int value) throws IndexOutOfBoundsException {
        sExtraFields[index] = value;
    }

    public void increaseExtraField(int index, int value) throws IndexOutOfBoundsException {
        sExtraFields[index] += value;
    }

    public int getIdentifier() {
        return sIdentifier;
    }

    public Effect getEffect() {
        return sEffect;
    }

}