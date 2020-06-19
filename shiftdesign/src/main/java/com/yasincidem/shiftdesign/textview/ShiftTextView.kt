package com.yasincidem.shiftdesign.textview

import android.content.Context
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.LineBackgroundSpan
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.yasincidem.shiftdesign.R
import com.yasincidem.shiftdesign.textview.snappable.PaddingBackgroundColorSpan
import com.yasincidem.shiftdesign.textview.snappable.TransparentBackgroundColorSpan

class ShiftTextView: AppCompatTextView {

    private lateinit var mSpan: LineBackgroundSpan
    private var color: Int = 0
    private var shouldDrawBorder: Boolean = false

    constructor(context: Context): super(context)

    constructor(context: Context, attrs: AttributeSet): super(context, attrs){
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet) {
        val attributeValues = context.obtainStyledAttributes(attrs, R.styleable.ShiftTextView)
        with(attributeValues) {
            try {
                color = getColor(R.styleable.ShiftTextView_borderColor, 0)
                shouldDrawBorder = !getBoolean(R.styleable.ShiftTextView_lazyDraw, false)
                mSpan = when(getInt(R.styleable.ShiftTextView_spanType, 0)) {
                    0 ->   {
                        PaddingBackgroundColorSpan(color, paddingEnd)
                    }
                    1 -> {
                        setTextColor(Color.rgb(250, 250, 250))
                        TransparentBackgroundColorSpan(paddingEnd)
                    }
                    else -> {
                        PaddingBackgroundColorSpan(color, paddingEnd)
                    }
                }
                if (shouldDrawBorder) {
                    applySpannable()
                }
            } finally {
                recycle()
            }
        }
    }

    fun drawBorder() {
        shouldDrawBorder = true
        invalidate()
    }

    private fun applySpannable() {
        setShadowLayer(paddingEnd.toFloat(), 0f, 0f, 0)
        val spannable = SpannableString(text)
        spannable.setSpan(mSpan, 0, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        text = spannable
    }
}