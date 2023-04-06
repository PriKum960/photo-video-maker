package com.aman043358.photovideomaker.views;

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import androidx.cardview.widget.CardView
import com.aman043358.photovideomaker.R

class ScaleCardLayout : CardView {
    var mAspectRatioHeight = 360
    var mAspectRatioWidth = 640
    var VIDEO_HEIGHT = 480
    var VIDEO_WIDTH = 720

    constructor(context: Context?) : super(context!!) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        Init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        Init(context, attrs)
    }

    @SuppressLint("ResourceType")
    private fun Init(context: Context, attrs: AttributeSet?) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.ScaleCardLayout)
        mAspectRatioWidth = a.getInt(0, VIDEO_WIDTH)
        mAspectRatioHeight = a.getInt(1, VIDEO_HEIGHT)
        a.recycle()
    }

    @SuppressLint("WrongConstant")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val finalWidth: Int
        val finalHeight: Int
        if (mAspectRatioHeight == mAspectRatioWidth) {
            super.onMeasure(widthMeasureSpec, widthMeasureSpec)
        }
        val originalWidth = MeasureSpec.getSize(widthMeasureSpec)
        val originalHeight = MeasureSpec.getSize(heightMeasureSpec)
        val calculatedHeight =
            ((mAspectRatioHeight * originalWidth).toFloat() / mAspectRatioWidth.toFloat()).toInt()
        if (calculatedHeight > originalHeight) {
            finalWidth =
                ((mAspectRatioWidth * originalHeight).toFloat() / mAspectRatioHeight.toFloat()).toInt()
            finalHeight = originalHeight
        } else {
            finalWidth = originalWidth
            finalHeight = calculatedHeight
        }
        super.onMeasure(
            MeasureSpec.makeMeasureSpec(finalWidth, 1073741824),
            MeasureSpec.makeMeasureSpec(finalHeight, 1073741824)
        )
    }
}