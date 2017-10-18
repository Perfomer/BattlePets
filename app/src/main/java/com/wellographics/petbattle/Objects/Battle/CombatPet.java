package com.wellographics.petbattle.Objects.Battle;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.wellographics.petbattle.Managers.ImageHandler;
import com.wellographics.petbattle.Objects.Pet;
import com.wellographics.petbattle.R;

public class CombatPet extends Pet {

    public static final int INDEX_MAIN = 0, INDEX_SECOND = 1, INDEX_THIRD = 3;

    public static final int MAX_SPELLS_IN_THE_BATTLE = 3;

    public static final int ANIM_INACTION = 0, ANIM_MOVING = 1, ANIM_DAMAGE = 3, ANIM_DEATH = 4,
        ANIM_SPELL1 = 5, ANIM_SPELL2 = 6, ANIM_SPELL3 = 7;

    private final float HEALTH_LEVEL_COEFF = 0.3f;

    private int pCurrentHealth, pCurrentStrength, pCurrentSpeed, pMaxHealth, pSimpleStrength,
            pSimpleSpeed, pOwnerLevel;

    private AnimationDrawable pADInaction, pADMoving, pADDeath, pADDamaged,
        pADSpells[];

    private boolean pFriendly, pActive = false;

    private Character pOwner;

    public CombatPet(Context context, int id, Character owner) {
        super(context, id);
        this.setOwner(owner);
        prepareVariables();
    }

    public void setOwner(Character owner) {
        pOwner = owner;
        pOwnerLevel = pOwner.getLevel().getLevelValue();
        pFriendly = pOwner.isFriendly();
    }

    private void prepareVariables() {
        pMaxHealth = (int) ((pPrimalHealth * (1 + (pOwnerLevel - 1) * HEALTH_LEVEL_COEFF)) * (1 + pRarity * RARITY_COEFFICIENT));
        pSimpleSpeed = (int) (pPrimalSpeed * pOwnerLevel * (1 + pRarity * RARITY_COEFFICIENT));
        pSimpleStrength = (int) (pPrimalStrength * pOwnerLevel * (1 + pRarity * RARITY_COEFFICIENT));

        pCurrentHealth = pMaxHealth;
        pCurrentSpeed = pSimpleSpeed;
        pCurrentStrength = pSimpleStrength;

        pADSpells = new AnimationDrawable[MAX_SPELLS_IN_THE_BATTLE];

        setAnimationVariables();
    }

    /** МЕТОД ПОЛУЧЕНИЯ РАЗМЕРА VIEW ДЛЯ ПИТОМЦА ИЗ РЕСУРСОВ
     *  Если в ресурсах отсутствует значение для конкретного питомца, будет возвращено
     *  стандартное значение (@link R.dimen.Pet_Size_Default);
     */
    public float getDimensionSize() {
        try {
            return pContext.getResources().getDimension(
                    pContext.getResources().getIdentifier(
                            "Pet_Size_" + String.valueOf(pIdentifier),
                            "dimen",
                            pContext.getPackageName()
                    ));
        }catch (Resources.NotFoundException e) {
            return pContext.getResources().getDimension(R.dimen.Pet_Size_default);
        }
    }

    /** МЕТОД ПОЛУЧЕНИЯ АНИМАЦИИ. Возможные параметры функции:
     * ANIM_INACTION — бездействие (invative)
     * ANIM_MOVING — передвижение (move)
     * ANIM_DAMAGE — получение урона (damaged)
     * ANIM_DEATH — смерть (death)
     * ANIM_SPELLN — N-ое заклинание
     * @param actionIdentifier — идентификатор действия
     * @return animation — объект класса AnimationDrawable
     */
    public AnimationDrawable getAnimation(int actionIdentifier) {
        switch (actionIdentifier) {
            case ANIM_INACTION:
                return pADInaction;
            case ANIM_MOVING:
                return pADMoving;
            case ANIM_DAMAGE:
                return pADDamaged;
            case ANIM_DEATH:
                return pADDeath;
            default:
                return pADSpells[actionIdentifier - ANIM_SPELL1];
        }
    }

    private void setAnimationVariables() throws Resources.NotFoundException{
        String path = "a" + String.valueOf(pIdentifier) + "_";

        pADInaction = reverseBitmapAnimations(
                (AnimationDrawable) pContext.getResources().getDrawable(
                        pContext.getResources().getIdentifier(
                                path + "inactive", "drawable", pContext.getPackageName()
                        ))
        );

        pADMoving = reverseBitmapAnimations(
                (AnimationDrawable) pContext.getResources().getDrawable(
                        pContext.getResources().getIdentifier(
                                path + "moving", "drawable", pContext.getPackageName()
                        ))
        );

        int i = 0;
        pADSpells[i] = reverseBitmapAnimations(
                (AnimationDrawable) pContext.getResources().getDrawable(
                        pContext.getResources().getIdentifier(
                                path + "sp_" + String.valueOf(i), "drawable", pContext.getPackageName()
                        ))
        );

        //Добавить ещё
    }

    /** МЕТОД ДЛЯ ОТЗЕРКАЛИВАНИЯ АНИМАЦИИ
     * @param animation — AnimationDrawable-объект
     * @return отзеркаленную анимацию, если это объект противника, иначе возвращает аргумент
     *         функции
     */
    private AnimationDrawable reverseBitmapAnimations(AnimationDrawable animation) {
        return pFriendly ? animation : ImageHandler.deployFrameAnimation(animation);
    }

    public TranslateAnimation getMovingAnimation() {
        TranslateAnimation anim = new TranslateAnimation(-400, 50, 0, 0); //хардкод дикий
        anim.setDuration(1000);
        anim.setFillAfter(true);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {}

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        return anim;
    }

    public void increaseHealth(int healthValue) {
        pCurrentHealth += healthValue;
        if (pCurrentHealth > pMaxHealth) pCurrentHealth = pMaxHealth;
        else if (pCurrentHealth < 0) pCurrentHealth = 0;
    }

    public void increaseStrength(int strengthValue) {
        pCurrentStrength += strengthValue;
        if (pCurrentStrength < 0) pCurrentStrength = 0;
    }

    public void increaseSpeed(int speedValue) {
        pCurrentSpeed += speedValue;
        if (pCurrentSpeed < 0) pCurrentSpeed = 0;
    }

    public void setActive(boolean active) {
        pActive = active;
    }

    public boolean isActive() {
        return pActive;
    }

    public boolean isAlive() {
        return pCurrentHealth > 0;
    }

    public void setFriendly(boolean friendly) {
        pFriendly = friendly;
    }

    public int getOwnerLevel() {
        return pOwnerLevel;
    }

    public int getCurrentHealth() {
        return pCurrentHealth;
    }

    public int getCurrentStrength() {
        return pCurrentStrength;
    }

    public int getCurrentSpeed() {
        return pCurrentSpeed;
    }

    public int getMaxHealth() {
        return pMaxHealth;
    }

    public int getMaxStrength() {
        return pSimpleStrength;
    }

    public int getMaxSpeed() {
        return pSimpleSpeed;
    }

    public boolean isFriendly() {
        return pFriendly;
    }

    public int getSpellId(int index) {
        return pSpells[index];
    }

    public Character getOwner() {
        return pOwner;
    }
}
