package com.yasincidem.shiftdesign.imageview.drawable

import android.graphics.*
import android.graphics.drawable.Drawable

class BorderDrawable(
    private val color: Int,
    private val paddingBottom: Float = 0f,
    private val paddingEnd: Float = 0f
) : Drawable() {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    private val path = Path().apply {
        fillType = Path.FillType.EVEN_ODD
    }

    override fun draw(canvas: Canvas) {
        paint.color = color
        canvas.drawPath(path, paint)
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSPARENT
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

    override fun onBoundsChange(bounds: Rect) {
        val boundsF = RectF(bounds)
        val width = boundsF.right
        val height = boundsF.bottom
        if (paddingBottom > 0 && paddingEnd > 0 && paddingBottom == paddingEnd) {
            val borderWidth = paddingBottom
            path.addRect(
                boundsF.left + width - borderWidth,
                boundsF.top + borderWidth,
                boundsF.right,
                boundsF.bottom,
                Path.Direction.CW)

            path.addRect(
                boundsF.left + borderWidth,
                boundsF.top + height - borderWidth,
                boundsF.right,
                boundsF.bottom,
                Path.Direction.CW)
        }
    }
}