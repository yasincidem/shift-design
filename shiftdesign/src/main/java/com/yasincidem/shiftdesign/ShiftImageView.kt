package com.yasincidem.shiftdesign

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.ColorFilter
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Rect
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.widget.AppCompatImageView
import com.yasincidem.shiftdesign.drawable.BorderDrawable

class ShiftImageView: AppCompatImageView {

    private var scale: Float = 0.5f
    private lateinit var mBorder: BorderDrawable
    private var shouldDrawBorder: Boolean = false
    private var connectEdges: Boolean = false

    constructor(context: Context): super(context)

    constructor(context: Context, attrs: AttributeSet): super(context, attrs){
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    fun drawBorder() {
        shouldDrawBorder = true
        invalidate()
    }

    private fun init(context: Context, attrs: AttributeSet) {
        setWillNotDraw(false)
        val attributeValues = context.obtainStyledAttributes(attrs, R.styleable.ShiftImageView)
        with(attributeValues) {
            try {
                val color = getColor(R.styleable.ShiftImageView_borderColor, 0)
                scale = getFloat(R.styleable.ShiftImageView_dimScale, 0.5f)
                shouldDrawBorder = !getBoolean(R.styleable.ShiftImageView_lazyDraw, false)
                connectEdges = getBoolean(R.styleable.ShiftImageView_connectEdges, false)
                mBorder = BorderDrawable(
                    color,
                    paddingBottom.toFloat(),
                    paddingEnd.toFloat(),
                    connectEdges
                )
            } finally {
                recycle()
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mBorder.setBounds(0, 0, w, h)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (shouldDrawBorder)
            mBorder.draw(canvas!!)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val filter = createDimFilter()
        when(event?.action) {
            MotionEvent.ACTION_DOWN -> {
                focus(filter)
                return true
            }
            MotionEvent.ACTION_SCROLL -> {
                release()
                return true
            }
            MotionEvent.ACTION_UP -> {
                if (isMotionEventInsideView(this, event))
                    performClick()
                release()
                return true
            }
            else -> return false
        }
    }

    private fun focus(filter: ColorFilter) {
        isPressed = true
        performFocusAnimation()
        applyColorFilter(filter)
    }

    private fun release() {
        isPressed = false
        performReleaseAnimation()
        clearColorFilters()
    }

    private fun applyColorFilter(filter: ColorFilter) {
        colorFilter = filter
        mBorder.colorFilter = filter
    }

    private fun clearColorFilters() {
        clearColorFilter()
        mBorder.clearColorFilter()
    }

    private fun performFocusAnimation() {
        val animation: Animation = AnimationUtils.loadAnimation(context, R.anim.rect_focused)
        startAnimation(animation)
    }

    private fun performReleaseAnimation() {
        val animation: Animation = AnimationUtils.loadAnimation(context, R.anim.rect_released)
        startAnimation(animation)
    }

    private fun createDimFilter(): ColorFilter {
        val colorMatrix = ColorMatrix()
        colorMatrix.setSaturation(0f)
        colorMatrix.setScale(scale, scale, scale, 1f)
        return ColorMatrixColorFilter(colorMatrix)
    }

    private fun isMotionEventInsideView(view: View, event: MotionEvent): Boolean {
        val viewRect = Rect(
            view.left,
            view.top,
            view.right,
            view.bottom
        )
        return viewRect.contains(
            view.left + event.x.toInt(),
            view.top + event.y.toInt()
        )
    }
}