package com.nineone.verificationcode.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.widget.RelativeLayout
import com.nineone.verificationcode.R

class SimpleShadowLayout : RelativeLayout {

    val paint: Paint by lazy {
        Paint()
    }
    var shadow: Int = 0
    var shadowRoundCorner: Int = 0
    var shadowAlpha: Float = 0F
    var shadowColor: Int

    constructor(context: Context) : this(context, null) {

    }

    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
        val typedArray =
            context.obtainStyledAttributes(attributeSet, R.styleable.SimpleShadowLayout)
        shadow =
            typedArray.getDimensionPixelSize(R.styleable.SimpleShadowLayout_shadow_radius_dp, 0)
        shadowRoundCorner = typedArray.getDimensionPixelSize(
            R.styleable.SimpleShadowLayout_shadow_round_corner_dp,
            0
        )
        shadowAlpha = typedArray.getFloat(R.styleable.SimpleShadowLayout_shadow_alpha, 0f)
        shadowColor = typedArray.getColor(
            R.styleable.SimpleShadowLayout_shadow_round_color,
            Color.parseColor("#000000")
        )
        typedArray.recycle()
        Log.e("shadowAlpha", "==="+shadowAlpha
                +"   shadow="+shadow
                +"   shadowRoundCorner="+shadowRoundCorner
                +"   shadowColor="+shadowColor
        );
        paint.apply {
            this.color = shadowColor
            this.alpha = (shadowAlpha * 255).toInt()
            this.maskFilter = BlurMaskFilter(shadow.toFloat(), BlurMaskFilter.Blur.OUTER)
            this.isAntiAlias = true
        }
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        Log.e("onDraw", "=====" + shadow)
        super.onDraw(canvas)
//        setLayerType(LAYER_TYPE_SOFTWARE, null)
        val pathBg = Path();
        pathBg.addRoundRect(
            RectF(
                paddingLeft.toFloat(), paddingTop.toFloat(),
                width.toFloat() - paddingRight, height.toFloat() - paddingBottom
            ),
            shadowRoundCorner.toFloat(),
            shadowRoundCorner.toFloat(), Path.Direction.CW
        )
        canvas?.drawPath(pathBg, paint)
    }

    override fun willNotDraw(): Boolean {
        return super.willNotDraw()
    }

    public fun setShadowByDp(shadowDp: Int) {
        shadow = (context.resources.displayMetrics.density * shadowDp).toInt();
        invalidate()

    }

}