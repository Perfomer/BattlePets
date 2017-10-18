package com.wellographics.petbattle.Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.wellographics.petbattle.Managers.ImageHandler;
import com.wellographics.petbattle.R;

public class RoundedCornerImageView extends ImageView {

    public static final int TYPE_EXTRA_PET = 0, TYPE_SPELL = 1, NO_FRAME = -1, TYPE_MAIN_PET = 2;

    public static final int STYLE_SILVER = -10, STYLE_GOLD = -11;


    private int rRadius = 5, rTypeId = TYPE_SPELL, rStyleId = STYLE_SILVER;

    private boolean rRoundMainImage = false, rFriendly = true;

    private Context rContext;

    public RoundedCornerImageView(Context context) {
        super(context);
        init(context, null);
    }

    public RoundedCornerImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RoundedCornerImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        rContext = context;

        if (attrs != null) {
            TypedArray a =
                    context.getTheme().obtainStyledAttributes(
                            attrs, R.styleable.RoundedCornerImageView, 0, 0);
            try {
                rRadius = a.getInteger(R.styleable.RoundedCornerImageView_radius, 5);
                rTypeId = a.getInteger(R.styleable.RoundedCornerImageView_frameType, TYPE_SPELL);
                rStyleId = a.getInteger(R.styleable.RoundedCornerImageView_frameStyle, STYLE_SILVER);
                rRoundMainImage = a.getBoolean(R.styleable.RoundedCornerImageView_roundMainImage, false);
                rFriendly = a.getBoolean(R.styleable.RoundedCornerImageView_deploy, true);
            } finally {
                a.recycle();
            }
        }

        this.setBackgroundDrawable(super.getBackground());
        setFrameStyle(rStyleId);
    }


    public void setFrameStyle(int styleId) {
        int resId = 0;
        switch (styleId) {
            case STYLE_SILVER:
                switch (rTypeId) {
                    case TYPE_SPELL:
                        setImageResource(R.drawable.general_spell_background_0);
                        break;
                    case TYPE_EXTRA_PET:
                        setImageResource(R.drawable.battle_toppanel_extrapets_iconbackground);
                        break;
                    case TYPE_MAIN_PET:
                        if (rFriendly)
                            setImageResource(R.drawable.battle_toppanel_mainpet_iconbackground);
                        else
                            setImageDrawable(ImageHandler.deployImage(rContext.getResources(), R.drawable.battle_toppanel_mainpet_iconbackground));
                }
                break;
            case STYLE_GOLD:
                switch (rTypeId) {
                    case TYPE_SPELL:
                        setImageResource(R.drawable.general_spell_background_1);
                }
        }
    }

    @Override
    public void setBackgroundResource(int resid) {
        this.setBackgroundDrawable(
                rContext.getResources().getDrawable(resid));
    }

    @Override
    public void setBackgroundDrawable(Drawable background) {
        if (background != null)
            super.setBackgroundDrawable(
                   // ImageHandler.getScaledDrawable(rContext.getResources(),
                            ImageHandler.getRoundedCornerDrawable(background, rRadius)
                    //)
            );
    }

    @Override
    public void setBackground(Drawable background) {
        this.setBackgroundDrawable(background);
    }

    public void setRadius(int radius) {
        rRadius = radius;
    }
}
