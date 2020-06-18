package com.yasincidem.shiftdesign

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.widget.AppCompatImageView
import com.yasincidem.shiftdesign.drawable.BorderDrawable


class ShiftImageView: AppCompatImageView {

    private lateinit var colorAnim: ValueAnimator
    private lateinit var mBorder: BorderDrawable
    private var mScale: Float = 0.7f
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
                mScale = getFloat(R.styleable.ShiftImageView_dimScale, 0.7f)
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
        when(event?.action) {
            MotionEvent.ACTION_DOWN -> {
                focus()
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

    private fun focus() {
        isPressed = true
        performFocusAnimation()
        applyColorFilter()
    }

    private fun release() {
        isPressed = false
        performReleaseAnimation()
        clearColorFilters()
    }

    private fun applyColorFilter() {
        colorAnim = ObjectAnimator.ofFloat(0.99f, mScale)
        colorAnim.addUpdateListener { animation ->
            val av = animation.animatedValue as Float
            colorFilter = createDimFilter(av)
            mBorder.colorFilter = createDimFilter(av)
        }
        colorAnim.duration = 100
        colorAnim.start()
    }

    private fun clearColorFilters() {
        colorAnim.reverse()
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

    private fun createDimFilter(scale: Float): ColorFilter {
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