package com.wellographics.petbattle.Managers;

import android.content.Context;
import android.graphics.Typeface;

import com.wellographics.petbattle.Views.FontView;

public class FontManager{

    private Typeface fmBold, fmMedium, fmLight, fmThin;

    public FontManager(Context context) {
        fmThin = Typeface.createFromAsset(context.getAssets(), "fonts/HelveticaNeueCyr-Thin.otf");
        fmLight = Typeface.createFromAsset(context.getAssets(), "fonts/HelveticaNeueCyr-Light.otf");
        fmMedium = Typeface.createFromAsset(context.getAssets(), "fonts/HelveticaNeueCyr-Medium.otf");
        fmBold = Typeface.createFromAsset(context.getAssets(), "fonts/HelveticaNeueCyr-Bold.otf");
    }

    public Typeface getFont(int fontId) {
        Typeface font = null;
        switch (fontId) {
            case FontView.FONT_BOLD: font = fmBold; break;
            case FontView.FONT_MEDIUM: font = fmMedium; break;
            case FontView.FONT_LIGHT: font = fmLight; break;
            case FontView.FONT_THIN: font = fmThin;
        }
        return font;
    }
}
