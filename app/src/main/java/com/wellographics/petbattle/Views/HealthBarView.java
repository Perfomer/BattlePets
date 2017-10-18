package com.wellographics.petbattle.Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.wellographics.petbattle.R;

import java.util.Arrays;

public class HealthBarView extends ImageView {

    public static final int LARGE = 1, SMALL = 0;

    public static final boolean FRIEND = true, ENEMY = false;

    private Bitmap hProgressImage, hBackupProgressImage;

    private Context hContext;

    private int hSizeId = LARGE, hBackgroundImageResourceId,
            hProgressImageResourceId, hPixelsArray[];

    private boolean hFriendly = FRIEND;

    public HealthBarView(Context context) {
        super(context);
        init(context, null);
    }

    public HealthBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public HealthBarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        hContext = context;

        if (attrs != null) {
            TypedArray a =
                    context.getTheme().obtainStyledAttributes(
                            attrs, R.styleable.HealthBarView, 0, 0);
            try {
                hSizeId = a.getInteger(R.styleable.HealthBarView_size, 0);
                hFriendly = a.getBoolean(R.styleable.HealthBarView_friendly, FRIEND);
            } finally {
                a.recycle();
            }
        }
        setMainBitmap();

        hPixelsArray = new int[hProgressImage.getWidth() * hProgressImage.getHeight()];
        Arrays.fill(hPixelsArray, Color.TRANSPARENT);
    }

    private void setMainBitmap() {
        switch (hSizeId) {
            case SMALL:
                hProgressImageResourceId = R.drawable.battle_toppanel_extrapets_healthbar_full;
                hBackgroundImageResourceId = R.drawable.battle_toppanel_extrapets_healthbar_empty;
                break;
            case LARGE:
                hProgressImageResourceId = R.drawable.battle_toppanel_healthbar_full;
                hBackgroundImageResourceId = R.drawable.battle_toppanel_healthbar_empty;
                break;
        }

        hProgressImage = BitmapFactory.decodeResource(hContext.getResources(),
                hProgressImageResourceId);
        hBackupProgressImage = hProgressImage;

        this.setImageBitmap(hProgressImage);
        this.setBackgroundResource(hBackgroundImageResourceId);
    }

    public boolean isFriendly() {
        return hFriendly;
    }

    public void setSize(int size) {
        hSizeId = size;
    }

    public void setFriendly(boolean friendly) {
        hFriendly = friendly;
    }

    public void setProgress(int completed, int total) {
        if (hProgressImage == null) setMainBitmap();

        Bitmap image = hBackupProgressImage.copy(Bitmap.Config.ARGB_8888, true);
        int pixel = completed * image.getWidth() / total;

        image.setPixels(
                hPixelsArray,
                0,
                image.getWidth(),
                hFriendly ? pixel : 0,
                0,
                image.getWidth() - pixel,
                image.getHeight());

        this.setImageBitmap(image);
    }


}
