package com.wellographics.petbattle.Managers;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;

public class SpriteCutter {

    private Bitmap sSpriteList;

    private int sSpriteHeight, sSpriteWidth, sSpriteRows, sSpriteColumns;

    public SpriteCutter(Bitmap spriteList, int spriteWidth, int spriteHeight, int rows, int columns) {
        sSpriteList = spriteList;
        sSpriteHeight = spriteHeight;
        sSpriteWidth = spriteWidth;
        sSpriteRows = rows;
        sSpriteColumns = columns;
    }

    public SpriteCutter(Resources res, int resId, int spriteWidth, int spriteHeight, int rows, int columns) throws Resources.NotFoundException {
        this(BitmapFactory.decodeResource(res, resId), spriteWidth, spriteHeight, rows, columns);
    }

    public Bitmap getSpriteBitmap(int xPos, int yPos) {
//        int srcX = currentFrame * width;
//        int srcY = 1 * height;
//        Rect src = new Rect(srcX, srcY, srcX + width, srcY + height);
//        Rect dst = new Rect(x, y, x + width, y + height);
//        canvas.drawBitmap(bmp, src, dst, null);
        return null;
    }

    public AnimationDrawable getSpriteAnimation(int rowPos, int[] duration, boolean oneShot) {
        return null;
    }

}
