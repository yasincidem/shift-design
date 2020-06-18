package com.yasincidem.shiftdesign.drawable

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.PixelFormat
import android.graphics.ColorFilter
import android.graphics.Rect
import android.graphics.drawable.Drawable

class BorderDrawable(
    private val color: Int,
    private val paddingBottom: Float = 0f,
    private val paddingEnd: Float = 0f,
    private val connectEdges: Boolean = false
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

            if (connectEdges) {
                path.fillType = Path.FillType.WINDING
                path.moveTo(boundsF.left + width - borderWidth, 0f)
                path.lineTo(boundsF.left + width + borderWidth, borderWidth)
                path.lineTo(boundsF.left + width - borderWidth, borderWidth)
                path.lineTo(boundsF.left + width - borderWidth, 0f)

                path.moveTo(0f, boundsF.left + height - borderWidth)
                path.lineTo(borderWidth, boundsF.left + width + borderWidth)
                path.lineTo(borderWidth, boundsF.left + width - borderWidth)
                path.lineTo(0f, boundsF.left + height - borderWidth)

                path.close()
            }
        }
    }
}