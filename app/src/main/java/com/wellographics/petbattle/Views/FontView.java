package com.wellographics.petbattle.Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

import com.wellographics.petbattle.Managers.FontManager;
import com.wellographics.petbattle.R;

public class FontView extends TextView {

    public static final int FONT_THIN = -10, FONT_LIGHT = -11, FONT_MEDIUM = -12, FONT_BOLD = -13;
    public static final int SHADOW_LIGHT = -9, SHADOW_DARK = -8;

    private FontManager mFontManager;
    private int mFontId = FONT_LIGHT, mShadowId = SHADOW_DARK;

    public FontView(Context context) {
        super(context);
        init(context, null);
    }

    public FontView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FontView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mFontManager = new FontManager(context);

        if (attrs != null) {
            TypedArray a =
                    context.getTheme().obtainStyledAttributes(attrs,R.styleable.FontView, 0, 0);
            try {
                mFontId = a.getInteger(R.styleable.FontView_font, FONT_LIGHT);
                mShadowId = a.getInteger(R.styleable.FontView_shadow, SHADOW_LIGHT);
            } finally {
                a.recycle();
            }
        }
        this.setPadding(getPaddingLeft() + 2, getPaddingTop() + 2, getPaddingRight() + 2, getPaddingBottom() + 2);
        this.setFont(mFontId);
        this.setShadowType(mShadowId);
    }

    private int getCustomShadowColor() {
        switch (mShadowId) {
            case SHADOW_LIGHT:
                return R.color.light_shadow;
            case SHADOW_DARK:
                return R.color.dark_shadow;
            default:
                return R.color.empty;
        }
    }

    public void setFont(int fontId) {
        this.setTypeface(mFontManager.getFont(fontId));
    }

    public void setShadowType(int shadowId) {
        mShadowId = shadowId;
        this.setShadowLayer(1, 1, 1, getCustomShadowColor());
    }

}
