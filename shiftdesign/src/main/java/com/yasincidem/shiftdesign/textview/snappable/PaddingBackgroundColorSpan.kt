package com.yasincidem.shiftdesign.textview.snappable

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.text.style.LineBackgroundSpan
import kotlin.math.roundToInt


class PaddingBackgroundColorSpan(
    private val backgroundColor: Int,
    private val padding: Int
): LineBackgroundSpan {

    private var mBgRect: Rect = Rect()

    override fun drawBackground(
        canvas: Canvas,
        paint: Paint,
        left: Int,
        right: Int,
        top: Int,
        baseline: Int,
        bottom: Int,
        text: CharSequence,
        start: Int,
        end: Int,
        lineNumber: Int
    ) {
        val textWidth = paint.measureText(text, start, end).roundToInt()
        val paintColor: Int = paint.color
        // Draw the background
        mBgRect.set(
            left + 30,
            top + 30,
            left + textWidth + padding,
            bottom + 10
        )

        paint.color = backgroundColor
        canvas.drawRect(mBgRect, paint)
        paint.color = paintColor
    }
}