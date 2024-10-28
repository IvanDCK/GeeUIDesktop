package com.letianpai.robot.desktop.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable

object RoundImageHelper {
    fun getRoundedDrawable(context: Context, drawable: Drawable, cornerRadiusDp: Int): Drawable {
        // Convert Drawable to Bitmap
        val bitmap = drawableToBitmap(drawable)


        // Convert Bitmap to Rounded Bitmap
        val roundedBitmap = getRoundedBitmap(bitmap, cornerRadiusDp)

        // Creating Rounded Corners Drawable
        return BitmapDrawable(context.resources, roundedBitmap)
    }

    private fun drawableToBitmap(drawable: Drawable): Bitmap {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }

        val width = drawable.intrinsicWidth
        val height = drawable.intrinsicHeight

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return bitmap
    }

    private fun getRoundedBitmap(bitmap: Bitmap, cornerRadiusDp: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val cornerRadius = (cornerRadiusDp * bitmap.density).toFloat()

        val output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)

        val paint = Paint()
        val rect = Rect(0, 0, width, height)
        val rectF = RectF(rect)

        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = -0x1
        canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, paint)

        paint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.SRC_IN))
        canvas.drawBitmap(bitmap, rect, rect, paint)

        return output
    }
}
