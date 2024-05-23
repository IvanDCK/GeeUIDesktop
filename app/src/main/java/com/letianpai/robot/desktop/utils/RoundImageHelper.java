package com.letianpai.robot.desktop.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class RoundImageHelper {

    public static Drawable getRoundedDrawable(Context context, Drawable drawable, int cornerRadiusDp) {
        // 将 Drawable 转换为 Bitmap
        Bitmap bitmap = drawableToBitmap(drawable);
        
        // 将 Bitmap 转换为圆角 Bitmap
        Bitmap roundedBitmap = getRoundedBitmap(bitmap, cornerRadiusDp);

        // 创建圆角 Drawable
        return new BitmapDrawable(context.getResources(), roundedBitmap);
    }

    private static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    private static Bitmap getRoundedBitmap(Bitmap bitmap, int cornerRadiusDp) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float cornerRadius = cornerRadiusDp * bitmap.getDensity();

        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, width, height);
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(0xFFFFFFFF);
        canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, paint);

        paint.setXfermode(new android.graphics.PorterDuffXfermode(android.graphics.PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
}
