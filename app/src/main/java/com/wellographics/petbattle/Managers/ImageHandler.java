package com.wellographics.petbattle.Managers;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.view.Gravity;

public class ImageHandler {

    /* МЕТОД, ВОЗВРАЩАЮЩИЙ ОТЗЕРКАЛЕННЫЙ (РАЗВЁРНУТЫЙ НА 180 ГРАДУСОВ) БИТМАП */
    public static Bitmap deployImage(Bitmap image) {
        Matrix matrix = new Matrix();
        matrix.preScale(-1.0f, 1.0f);
        return Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, false);
    }

    /* МЕТОД, ВОЗВРАЩАЮЩИЙ ОТЗЕРКАЛЕННУЮ (РАЗВЁРНУТУЮ НА 180 ГРАДУСОВ) ПОКАДРОВУЮ АНИМАЦИЮ */
    public static AnimationDrawable deployFrameAnimation(AnimationDrawable animation) {
        BitmapDrawable[] frameArray = new BitmapDrawable[animation.getNumberOfFrames()];
        int[] durationArray = new int[frameArray.length];
        for (int i = 0; i < frameArray.length; i++) {
            Bitmap frame = deployImage(
                    ((BitmapDrawable) animation.getFrame(i)).getBitmap());
            animation.getDuration(i);
            durationArray[i] = animation.getDuration(i);
            frameArray[i] = new BitmapDrawable(Bitmap.createBitmap(frame));
        }
        AnimationDrawable animationDrawable = new AnimationDrawable();
        animationDrawable.setOneShot(animation.isOneShot());
        for (int i = 0; i < frameArray.length; i++)
            animationDrawable.addFrame(frameArray[i], durationArray[i]);
        return animationDrawable;
    }


    public static Drawable deployImage(Resources res, int drawableId) {
        return new BitmapDrawable(
                res, deployImage(BitmapFactory.decodeResource(res, drawableId))
        );
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int radius) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawRoundRect(rectF, radius, radius, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static Drawable getRoundedCornerDrawable(Resources res, int drawableId, int radius) {
        return new BitmapDrawable(
                res, getRoundedCornerBitmap(BitmapFactory.decodeResource(res, drawableId), radius)
        );
    }

    public static Drawable getRoundedCornerDrawable(Drawable drawable, int radius) {
        return new BitmapDrawable(
                getRoundedCornerBitmap(((BitmapDrawable) drawable).getBitmap(), radius));
    }

    public static Drawable getScaledDrawable(Resources res, Drawable drawable) {
        int padding = 15;
        ScaleDrawable sDrawable = new ScaleDrawable(drawable,
                Gravity.CENTER,
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        sDrawable.setLevel(2000);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        return new BitmapDrawable(res, bitmap);
    }

    public static int getAnimationDrawableDuration(AnimationDrawable animationDrawable) {
        int duration = 0;
        for (int i = 0; i < animationDrawable.getNumberOfFrames(); i++) duration += animationDrawable.getDuration(i);
        return duration;
    }

}
