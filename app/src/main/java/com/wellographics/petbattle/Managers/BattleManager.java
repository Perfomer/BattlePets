package com.wellographics.petbattle.Managers;

import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.wellographics.petbattle.Activities.BattleActivity;
import com.wellographics.petbattle.Managers.Adapters.EffectAdapter;
import com.wellographics.petbattle.Objects.Battle.Bundles.BattleActionResult;
import com.wellographics.petbattle.Objects.Battle.Bundles.BattleData;
import com.wellographics.petbattle.Objects.Battle.Bundles.TurnResult;
import com.wellographics.petbattle.Objects.Battle.CombatPet;
import com.wellographics.petbattle.Objects.Battle.Effect;
import com.wellographics.petbattle.Objects.Battle.Spell;
import com.wellographics.petbattle.R;
import com.wellographics.petbattle.Views.FontView;
import com.wellographics.petbattle.Views.HealthBarView;
import com.wellographics.petbattle.Views.RoundedCornerImageView;

import java.util.PriorityQueue;
import java.util.Random;

public class BattleManager {

    private final int MAX_BUTTONS_IN_THE_BOTTOM_PANEL = 6;
    private final int STRIKER_AREA = 1, DEFENDER_AREA = 2;

    private BattleActivity bActivity;

    private RelativeLayout bMainLayout, bEnemyLayout, bPlayerLayout;

    private ImageButton bPlayerMainPetType, bEnemyMainPetType;

    private ImageView bPlayerView, bEnemyView, bSpellView1, bSpellView2, bSpellView3,
            bPlayerTurnIndicatorUpper, bPlayerTurnIndicatorLower, bEnemyTurnIndicatorUpper,
            bEnemyTurnIndicatorLower;

    private RoundedCornerImageView bPlayerMainPetIcon, bPlayerExtraPetsFirstIcon,
            bPlayerExtraPetsSecondIcon, bEnemyMainPetIcon, bEnemyExtraPetsFirstIcon,
            bEnemyExtraPetsSecondIcon;

    private FontView bPlayerPetName, bPlayerPetLevel, bEnemyPetName, bEnemyPetLevel,
            bPlayerMainPetHealthBarText, bEnemyMainPetHealthBarText;

    private HealthBarView bPlayerExtraPetsFirstHealthBar, bPlayerExtraPetsSecondHealthBar,
            bPlayerMainPetHealthBar, bEnemyExtraPetsFirstHealthBar, bEnemyExtraPetsSecondHealthBar,
            bEnemyMainPetHealthBar;

    private GridView bPlayerEffects, bEnemyEffects;

    private Animation bBreath, bMoving, bFlying, bBlinking, bTextNormalRise, bTextCriticalRise,
            bShake;


    public BattleManager(BattleActivity battleActivity) {
        bActivity = battleActivity;
        BattleViewLoader loader = new BattleViewLoader(battleActivity);

        bBreath = AnimationUtils.loadAnimation(bActivity, R.anim.breath);
        bFlying = AnimationUtils.loadAnimation(bActivity, R.anim.breath);
        bBlinking = AnimationUtils.loadAnimation(bActivity, R.anim.blinking);
        bTextNormalRise = AnimationUtils.loadAnimation(bActivity, R.anim.rise_normal);
        bTextCriticalRise = AnimationUtils.loadAnimation(bActivity, R.anim.rise_crit);
        bShake = AnimationUtils.loadAnimation(bActivity, R.anim.shake);
    }


    /**
     * МЕТОД, ВОСПРОИЗВОДЯЩИЙ ДЕЙСТВИЯ ТЕКУЩЕГО ХОДА
     * И всё соответствующее, например всплывающая надпись о результате атаки
     *
     * @param turn — информация о ходе
     */
    public void dealTurn(final TurnResult turn) {
        int fightingArea = 0;

        setEnabledSpellButtons(false);

        final CombatPet striker = turn.getAttacker(), defender = turn.getDefender();
        final Spell strikerAction = turn.getStrikerSpell(), defenderAction = turn.getDefenderSpell();
        AnimationQueue strikerQueue = new AnimationQueue(), defenderQueue = new AnimationQueue();

        final AnimationDrawable
                actionByStriker = striker.getAnimation(CombatPet.ANIM_SPELL1),
                actionByDefender = defender.getAnimation(CombatPet.ANIM_SPELL1),
                //damageForDefender = strikerAction.isAttack() ? defender.getAnimation(CombatPet.ANIM_DAMAGE) : null,
                //damageForStriker = defenderAction.isAttack() ? striker.getAnimation(CombatPet.ANIM_DAMAGE) : null,
                strikerMoving = striker.getAnimation(CombatPet.ANIM_MOVING),
                defenderMoving = defender.getAnimation(CombatPet.ANIM_MOVING),
                strikerInaction = striker.getAnimation(CombatPet.ANIM_INACTION),
                defenderInaction = defender.getAnimation(CombatPet.ANIM_INACTION),
                strikerDeath = striker.getAnimation(CombatPet.ANIM_DEATH),
                defenderDeath = defender.getAnimation(CombatPet.ANIM_DEATH);

        final ImageView
                strikerView = (striker.isFriendly() ? bPlayerView : bEnemyView),
                defenderView = (defender.isFriendly() ? bPlayerView : bEnemyView);

        final RelativeLayout
                strikerLayout = (striker.isFriendly() ? bPlayerLayout : bEnemyLayout),
                defenderLayout = (defender.isFriendly() ? bPlayerLayout : bEnemyLayout);

        if (strikerAction.isMovable()) fightingArea = DEFENDER_AREA;
        else if (defenderAction.isMovable()) fightingArea = STRIKER_AREA;


        switch (fightingArea) {
            case DEFENDER_AREA:
                fullAction(true, striker, strikerView, defenderView, strikerLayout, strikerMoving, actionByStriker, actionByDefender,
                        strikerInaction, defenderInaction, turn.getActionResult(), turn.getAnswerResult());
                break;
            case STRIKER_AREA:
                Handler handler = new Handler();
                Runnable justAction = new Runnable() {
                    @Override
                    public void run() {
                        if (strikerAction.isAttack())
                            createSwimmingDamage(defender, turn.getActionResult(), false);
                        startFrameAnimation(strikerInaction, strikerView);
                        fullAction(false, defender, defenderView, strikerView, defenderLayout, defenderMoving, actionByDefender, null,
                                defenderInaction, strikerInaction, turn.getAnswerResult(), null);
                    }
                };
                startFrameAnimation(actionByStriker, strikerView);
                handler.postDelayed(justAction, ImageHandler.getAnimationDrawableDuration(actionByStriker));
                break;
        }
    }

    private int fullAction(final boolean withAnswer, final CombatPet movable, final ImageView movableView, final ImageView defenderView,
                           final RelativeLayout movableLayout, final AnimationDrawable movableMoving, final AnimationDrawable movableAction, final AnimationDrawable defenderAction,
                           final AnimationDrawable movableInaction, final AnimationDrawable defenderInaction,
                           final BattleActionResult actionResult, final BattleActionResult answerResult) {
        moveAndAttack(movable, movableView, movableLayout, movableMoving, movableAction, actionResult);
        final TranslateAnimation moving = getMovingAnimation(false, movable.isFriendly());
        final Handler bHandlerTimers = new Handler();
        final Runnable
                rTurnEnd = new Runnable() {
            @Override
            public void run() {
                startFrameAnimation(movableInaction, movableView);
                setEnabledSpellButtons(true);
                setBlinkingTurnIndicator(movable.isFriendly());
            }
        },
                rReturning = new Runnable() {
                    @Override
                    public void run() {
                        movePet(false, movableView, movableLayout, movableMoving, moving);
                        startFrameAnimation(defenderInaction, defenderView);
                        bHandlerTimers.postDelayed(rTurnEnd, moving.getDuration());
                    }
                },
                rAnswerAttack = new Runnable() {
                    @Override
                    public void run() {
                        if (withAnswer) {
                            startFrameAnimation(defenderAction, defenderView);
                            createSwimmingDamage(movable, answerResult, movable.isFriendly());
                        }
                        bHandlerTimers.postDelayed(rReturning, withAnswer ? ImageHandler.getAnimationDrawableDuration(defenderAction) + 500 : 100);
                    }
                };
        int attackDelay = ImageHandler.getAnimationDrawableDuration(movableAction) + 3000;
        bHandlerTimers.postDelayed(rAnswerAttack, attackDelay);
        return (int) (attackDelay + (withAnswer ? ImageHandler.getAnimationDrawableDuration(defenderAction) + 500 : 100) + moving.getDuration());
    }

    private void moveAndAttack(final CombatPet movable, final ImageView movableView, RelativeLayout movableLayout, AnimationDrawable movingAnimation, final AnimationDrawable actionAnimation,
                               final BattleActionResult result) {
        AnimationDrawable movingFrameAnimation = movingAnimation;
        if (movable.isFriendly())
            movingFrameAnimation = ImageHandler.deployFrameAnimation(movingFrameAnimation);
        startFrameAnimation(movingFrameAnimation, movableView);
        final Runnable timer = new Runnable() {
            @Override
            public void run() {
                createSwimmingDamage(movable, result, !movable.isFriendly());
            }
        };
        TranslateAnimation translateAnimation = getMovingAnimation(true, movable.isFriendly());
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startFrameAnimation(actionAnimation, movableView);
                new Handler().postDelayed(timer, ImageHandler.getAnimationDrawableDuration(actionAnimation) - 200);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        movableLayout.startAnimation(translateAnimation);
    }

    private void movePet(boolean forward, ImageView movableView, RelativeLayout movableLayout, AnimationDrawable movingAnimation, TranslateAnimation translateAnimation) {
        AnimationDrawable movingFrameAnimation = movingAnimation;
        if (!forward)
            movingFrameAnimation = ImageHandler.deployFrameAnimation(movingFrameAnimation);
        startFrameAnimation(movingFrameAnimation, movableView);
        movableLayout.startAnimation(translateAnimation);
    }

    /**
     * МЕТОД, ВОЗВРАЩАЮЩИЙ АНИМАЦИЮ ПЕРЕДВИЖЕНИЯ
     *
     * @param forward — движение вперёд
     */
    private TranslateAnimation getMovingAnimation(boolean forward, boolean friendly) {
        TranslateAnimation movingTranslateAnimation = new TranslateAnimation(
                (friendly ? forward : !forward) ? Animation.RELATIVE_TO_PARENT : Animation.RELATIVE_TO_SELF, (friendly ? !forward : forward) ? 0 : -0.55f,
                (friendly ? !forward : forward) ? Animation.RELATIVE_TO_PARENT : Animation.RELATIVE_TO_SELF, (friendly ? forward : !forward) ? 0 : -0.55f,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0);
        movingTranslateAnimation.setDuration(1650);
        movingTranslateAnimation.setFillEnabled(true);
        movingTranslateAnimation.setFillAfter(true);
        movingTranslateAnimation.setInterpolator(new LinearInterpolator());

        return movingTranslateAnimation;
    }

    public void createSwimmingDamage(CombatPet defender, BattleActionResult result, boolean playerArea) {
        Random random = new Random();
        Resources res = bActivity.getResources();
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        boolean critical = result.isCritical();
        final FontView actionText = new FontView(bActivity);
        Animation fadeOut = AnimationUtils.loadAnimation(bActivity, critical ? R.anim.rise_crit : R.anim.rise_normal);
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                actionText.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        String text = String.valueOf(result.getFinallyDamage());

        int width = (int) res.getDimension(R.dimen.Battle_SwimmingText_marginHorizontal) + random.nextInt(125),
                height = (int) res.getDimension(R.dimen.Battle_SwimmingText_marginBottom) + random.nextInt(150);

        if (defender.isFriendly() == playerArea)
            width += (int) res.getDimension(R.dimen.Battle_SwimmingText_additionalMarginHorizontal);

        layoutParams.addRule(playerArea ? RelativeLayout.ALIGN_PARENT_LEFT : RelativeLayout.ALIGN_PARENT_RIGHT);

        layoutParams.setMargins(playerArea ? width : 0, height, playerArea ? 0 : width, 0);

        if (result.isStrong())
            text += " (" + bActivity.getResources().getString(R.string.strong) + ")";
        else if (result.isWeak())
            text += " (" + bActivity.getResources().getString(R.string.weak) + ")";

        actionText.setLayoutParams(layoutParams);
        actionText.setTextColor(res.getColor(critical ? R.color.clear_yellow : R.color.white));
        actionText.setFont(critical ? FontView.FONT_BOLD : FontView.FONT_MEDIUM);
        actionText.setTextSize(res.getDimension(critical ? R.dimen.Battle_SwimmingText_Size_critical : R.dimen.Battle_SwimmingText_Size_normal));
        actionText.setText(text);

        setHealthBarProgress(defender, CombatPet.INDEX_MAIN);
        bMainLayout.addView(actionText);
        actionText.startAnimation(fadeOut);
        if (critical) bMainLayout.startAnimation(bShake);
    }

    private void startFrameAnimation(AnimationDrawable animation, ImageView view) {
        view.setImageDrawable(animation);
        animation.start();
    }

    public void setHealthBarProgress(CombatPet pet, int indexOfPet) {
        HealthBarView hb = null;
        boolean friendly = pet.isFriendly();
        switch (indexOfPet) {
            case 0:
                hb = friendly ? bPlayerMainPetHealthBar : bEnemyMainPetHealthBar;
                (friendly ? bPlayerMainPetHealthBarText : bEnemyMainPetHealthBarText).setText(
                        String.valueOf(pet.getCurrentHealth()) + "/" +
                                String.valueOf(pet.getMaxHealth())
                );
                break;
            case 1:
                hb = friendly ? bPlayerExtraPetsFirstHealthBar : bEnemyExtraPetsFirstHealthBar;
                break;
            case 2:
                hb = friendly ? bPlayerExtraPetsSecondHealthBar : bEnemyExtraPetsSecondHealthBar;
        }
        hb.setProgress(pet.getCurrentHealth(), pet.getMaxHealth());
    }

    public void setPreviewIcon(CombatPet pet, int indexOfPet) {
        RoundedCornerImageView ib = null;
        boolean friendly = pet.isFriendly();
        switch (indexOfPet) {
            case 0:
                ib = friendly ? bPlayerMainPetIcon : bEnemyMainPetIcon;
                break;
            case 1:
                ib = friendly ? bPlayerExtraPetsFirstIcon : bEnemyExtraPetsFirstIcon;
                break;
            case 2:
                ib = friendly ? bPlayerExtraPetsSecondIcon : bEnemyExtraPetsSecondIcon;
        }
        int imageRes = pet.getPreviewDrawable();

        if (friendly) ib.setBackgroundResource(imageRes);
        else ib.setBackgroundDrawable(ImageHandler.deployImage(bActivity.getResources(), imageRes));
    }

    public void setName(CombatPet pet) {
        FontView name = pet.isFriendly() ? bPlayerPetName : bEnemyPetName;
        name.setText(pet.getName());
    }

    public void setLevel(CombatPet pet) {
        FontView level = pet.isFriendly() ? bPlayerPetLevel : bEnemyPetLevel;
        level.setText(String.valueOf(pet.getOwnerLevel()));
    }

    public void setTypeIcon(CombatPet pet) {
        ImageButton type = pet.isFriendly() ? bPlayerMainPetType : bEnemyMainPetType;
        type.setImageResource(pet.getType().getTypeIcon(bActivity));
    }

    public void setBlinkingTurnIndicator(boolean friendly) {
        ImageView views[] = {
                (friendly ? bPlayerTurnIndicatorLower : bEnemyTurnIndicatorLower),
                (friendly ? bPlayerTurnIndicatorUpper : bEnemyTurnIndicatorUpper),
                (friendly ? bEnemyTurnIndicatorLower : bPlayerTurnIndicatorLower),
                (friendly ? bEnemyTurnIndicatorUpper : bPlayerTurnIndicatorUpper)};

        for (View v : views) {
            v.setVisibility(View.VISIBLE);
            v.clearAnimation();
        }

        views[0].setImageResource(
                friendly ?
                        R.drawable.battle_toppanel_indicator_l_on :
                        R.drawable.battle_toppanel_indicator_r_on);

        views[1].setImageResource(friendly ?
                R.drawable.battle_toppanel_indicator_l_light :
                R.drawable.battle_toppanel_indicator_r_light);
        views[1].startAnimation(bBlinking);

        views[2].setImageResource(friendly ?
                R.drawable.battle_toppanel_indicator_r_off :
                R.drawable.battle_toppanel_indicator_l_off);

        views[3].setVisibility(View.INVISIBLE);
    }

    public void setEnabledSpellButtons(boolean value) {
        bSpellView1.setEnabled(value);
        bSpellView2.setEnabled(value);
        bSpellView3.setEnabled(value);
    }

    public void setInterfaceData(BattleData battleData) {
        CombatPet[] playerPets = battleData.getPetsArray(true), enemyPets = battleData.getPetsArray(false);

        bSpellView1.setBackgroundResource(new Spell(bActivity, playerPets[0].getSpellId(0)).getIconResource());
        bSpellView2.setBackgroundResource(new Spell(bActivity, playerPets[0].getSpellId(1)).getIconResource());
        bSpellView3.setBackgroundResource(new Spell(bActivity, playerPets[0].getSpellId(2)).getIconResource());

        setName(playerPets[CombatPet.INDEX_MAIN]);
        setTypeIcon(playerPets[CombatPet.INDEX_MAIN]);
        setLevel(playerPets[CombatPet.INDEX_MAIN]);
        setName(enemyPets[CombatPet.INDEX_MAIN]);
        setTypeIcon(enemyPets[CombatPet.INDEX_MAIN]);
        setLevel(enemyPets[CombatPet.INDEX_MAIN]);

        for (CombatPet pet : playerPets) {
            setHealthBarProgress(pet, battleData.getPlayer().getIndexOfPet(pet));
            setPreviewIcon(pet, battleData.getPlayer().getIndexOfPet(pet));
        }
        for (CombatPet pet : enemyPets) {
            setHealthBarProgress(pet, battleData.getEnemy().getIndexOfPet(pet));
            setPreviewIcon(pet, battleData.getEnemy().getIndexOfPet(pet));
        }

        ViewGroup.LayoutParams playerLayoutParams = bPlayerLayout.getLayoutParams();
        playerLayoutParams.height =
                (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        playerPets[CombatPet.INDEX_MAIN].getDimensionSize(),
                        bActivity.getResources().getDisplayMetrics());
        bPlayerLayout.setLayoutParams(playerLayoutParams);

        ViewGroup.LayoutParams enemyLayoutParams = bEnemyLayout.getLayoutParams();
        enemyLayoutParams.height =
                (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        enemyPets[CombatPet.INDEX_MAIN].getDimensionSize(),
                        bActivity.getResources().getDisplayMetrics());
        bEnemyLayout.setLayoutParams(enemyLayoutParams);

        bPlayerView.setImageDrawable(
                playerPets[CombatPet.INDEX_MAIN].getAnimation(CombatPet.ANIM_INACTION));
        ((AnimationDrawable) bPlayerView.getDrawable()).start();
        bPlayerLayout.startAnimation(bBreath);

        bEnemyView.setImageDrawable(
                enemyPets[CombatPet.INDEX_MAIN].getAnimation(CombatPet.ANIM_INACTION));
        ((AnimationDrawable) bEnemyView.getDrawable()).start();
        bEnemyLayout.startAnimation(bBreath);

        refreshEffectsAdapters(battleData.getPlayer().getEffects(), battleData.getEnemy().getEffects());


    }

    public void refreshEffectsAdapters(Effect[] playerEffects, Effect[] enemyEffects) {
        bPlayerEffects.setAdapter(new EffectAdapter(bActivity, playerEffects));
        bEnemyEffects.setAdapter(new EffectAdapter(bActivity, enemyEffects));
    }

    class AnimationQueue {

        PriorityQueue<TranslateAnimation> aAnimationsList;

        private AnimationQueue() {
            aAnimationsList = new PriorityQueue<>();
        }

        public void add(TranslateAnimation translateAnimation) {
            aAnimationsList.add(translateAnimation);

        }

        public TranslateAnimation getAnim() {
            return aAnimationsList.poll();
        }
    }


    class BattleViewLoader {

        private int vLoadedElements = 0, vAllElements = 33;
        private FontView vProgressViewer;
        private RelativeLayout vLoadingLayout;
        private BattleActivity vActivity;

        private BattleViewLoader(BattleActivity activity) {
            vActivity = activity;
            prepareLoading();
            initializeViews();
        }

        private void prepareLoading() {
            vLoadingLayout = (RelativeLayout) vActivity.findViewById(R.id.LoadingScreen_Layout);
            vProgressViewer = (FontView) vActivity.findViewById(R.id.LoadingScreen_TextIndicator);
            setProgress();
        }

        private void finishLoading() {
            vLoadingLayout.setVisibility(View.GONE);
        }

        private void loadView(View view) {
            view.setVisibility(View.VISIBLE);
            vLoadedElements++;
            setProgress();

            if (vLoadedElements == vAllElements) finishLoading();
        }

        private void setProgress() {
            int progress = 100 * vLoadedElements / vAllElements;
            vProgressViewer.setText(String.valueOf(progress) + " / 100");
        }

        private void initializeViews() {
            bMainLayout = (RelativeLayout) bActivity.findViewById(R.id.Battle_MainLayout);
            loadView(bMainLayout);
            bSpellView1 = (ImageView) bMainLayout.findViewWithTag("0");
            loadView(bSpellView1);
            bSpellView2 = (ImageView) bMainLayout.findViewWithTag("2");
            loadView(bSpellView2);
            bSpellView3 = (ImageView) bMainLayout.findViewWithTag("4");
            loadView(bSpellView3);

            bPlayerEffects = (GridView) bMainLayout.findViewById(R.id.Battle_TopPanel_Player_Effects);
            loadView(bPlayerEffects);
            bEnemyEffects = (GridView) bMainLayout.findViewById(R.id.Battle_TopPanel_Enemy_Effects);
            loadView(bEnemyEffects);

            bPlayerView = (ImageView) bMainLayout.findViewById(R.id.Battle_MobView_Player_IV);
            loadView(bPlayerView);
            bPlayerLayout = (RelativeLayout) bActivity.findViewById(R.id.Battle_MobViewLayout_Player_RL);
            loadView(bPlayerLayout);
            bEnemyView = (ImageView) bMainLayout.findViewById(R.id.Battle_MobView_Enemy_IV);
            loadView(bEnemyView);
            bEnemyLayout = (RelativeLayout) bActivity.findViewById(R.id.Battle_MobViewLayout_Enemy_RL);
            loadView(bEnemyLayout);

            bPlayerMainPetHealthBar = (HealthBarView) bActivity.findViewById(
                    R.id.Battle_TopPanel_Player_MainPet_HealthBar);
            loadView(bPlayerMainPetHealthBar);
            bPlayerMainPetIcon = (RoundedCornerImageView) bActivity.findViewById(
                    R.id.Battle_TopPanel_Player_MainPet_Icon);
            loadView(bPlayerMainPetIcon);
            bPlayerPetName = (FontView) bActivity.findViewById(
                    R.id.Battle_TopPanel_Player_MainPet_Name);
            loadView(bPlayerPetName);
            bPlayerPetLevel = (FontView) bActivity.findViewById(
                    R.id.Battle_TopPanel_Player_MainPet_Level);
            loadView(bPlayerPetLevel);
            bPlayerMainPetHealthBarText = (FontView) bActivity.findViewById(
                    R.id.Battle_TopPanel_Player_MainPet_HealthBar_Text);
            loadView(bPlayerMainPetHealthBarText);
            bPlayerExtraPetsFirstIcon = (RoundedCornerImageView) bActivity.findViewById(
                    R.id.Battle_TopPanel_Player_ExtraPets_First_Icon);
            loadView(bPlayerExtraPetsFirstIcon);
            bPlayerExtraPetsSecondIcon = (RoundedCornerImageView) bActivity.findViewById(
                    R.id.Battle_TopPanel_Player_ExtraPets_Second_Icon);
            loadView(bPlayerExtraPetsSecondIcon);
            bPlayerExtraPetsFirstHealthBar = (HealthBarView) bActivity.findViewById(
                    R.id.Battle_TopPanel_Player_ExtraPets_First_Healthbar);
            loadView(bPlayerExtraPetsFirstHealthBar);
            bPlayerExtraPetsSecondHealthBar = (HealthBarView) bActivity.findViewById(
                    R.id.Battle_TopPanel_Player_ExtraPets_Second_Healthbar);
            loadView(bPlayerExtraPetsSecondHealthBar);
            bPlayerTurnIndicatorUpper = (ImageView) bActivity.findViewById(
                    R.id.Battle_TopPanel_Player_TurnIndicator_Upper);
            loadView(bPlayerTurnIndicatorUpper);
            bPlayerTurnIndicatorLower = (ImageView) bActivity.findViewById(
                    R.id.Battle_TopPanel_Player_TurnIndicator_Lower);
            loadView(bPlayerTurnIndicatorLower);
            bPlayerMainPetType = (ImageButton) bActivity.findViewById(
                    R.id.Battle_TopPanel_Player_MainPet_Type);
            loadView(bPlayerMainPetType);

            bEnemyMainPetHealthBar = (HealthBarView) bActivity.findViewById(
                    R.id.Battle_TopPanel_Enemy_MainPet_HealthBar);
            loadView(bEnemyMainPetHealthBar);
            bEnemyMainPetIcon = (RoundedCornerImageView) bActivity.findViewById(
                    R.id.Battle_TopPanel_Enemy_MainPet_Icon);
            loadView(bEnemyMainPetIcon);
            bEnemyPetName = (FontView) bActivity.findViewById(
                    R.id.Battle_TopPanel_Enemy_MainPet_Name);
            loadView(bEnemyPetName);
            bEnemyPetLevel = (FontView) bActivity.findViewById(
                    R.id.Battle_TopPanel_Enemy_MainPet_Level);
            loadView(bEnemyPetLevel);
            bEnemyMainPetHealthBarText = (FontView) bActivity.findViewById(
                    R.id.Battle_TopPanel_Enemy_MainPet_HealthBar_Text);
            loadView(bEnemyMainPetHealthBarText);
            bEnemyExtraPetsFirstIcon = (RoundedCornerImageView) bActivity.findViewById(
                    R.id.Battle_TopPanel_Enemy_ExtraPets_First_Icon);
            loadView(bEnemyExtraPetsFirstIcon);
            bEnemyExtraPetsSecondIcon = (RoundedCornerImageView) bActivity.findViewById(
                    R.id.Battle_TopPanel_Enemy_ExtraPets_Second_Icon);
            loadView(bEnemyExtraPetsSecondIcon);
            bEnemyExtraPetsFirstHealthBar = (HealthBarView) bActivity.findViewById(
                    R.id.Battle_TopPanel_Enemy_ExtraPets_First_Healthbar);
            loadView(bEnemyExtraPetsFirstHealthBar);
            bEnemyExtraPetsSecondHealthBar = (HealthBarView) bActivity.findViewById(
                    R.id.Battle_TopPanel_Enemy_ExtraPets_Second_Healthbar);
            loadView(bEnemyExtraPetsSecondHealthBar);
            bEnemyTurnIndicatorUpper = (ImageView) bActivity.findViewById(
                    R.id.Battle_TopPanel_Enemy_TurnIndicator_Upper);
            loadView(bEnemyTurnIndicatorUpper);
            bEnemyTurnIndicatorLower = (ImageView) bActivity.findViewById(
                    R.id.Battle_TopPanel_Enemy_TurnIndicator_Lower);
            loadView(bEnemyTurnIndicatorLower);
            bEnemyMainPetType = (ImageButton) bActivity.findViewById(
                    R.id.Battle_TopPanel_Enemy_MainPet_Type);
            loadView(bEnemyMainPetType);
        }

    }
}