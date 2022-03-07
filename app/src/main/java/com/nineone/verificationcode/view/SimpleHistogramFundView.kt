package com.nineone.verificationcode.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import kotlinx.coroutines.yield

class SimpleHistogramFundView : View {
    private var textTitleBottomOffset = 5
    private var textDataBottomOffset = 8
    private var histogramWidth: Float = 0F
    private var histogramMargin: Float = 0F
    var textTitleTopPadding: Float? = null
    var textTitleBottomPadding: Float? = null
    var paint: Paint? = null
    var textPaint: TextPaint? = null
    private var strings: Array<String> = arrayOf("持有1年", "持有3年", "持有5年")
    private var lineY: Float = 0F
    private var textHeight: Float = 0F

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        if (textPaint == null) {
            textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
        }
        textPaint?.let {
            it.density = resources.displayMetrics.density
            it.textSize = resources.displayMetrics.density * 12
        }
        if (paint == null) paint = Paint()
        textTitleTopPadding = resources.displayMetrics.density * 12
        textTitleBottomPadding = resources.displayMetrics.density * 12
        histogramMargin = resources.displayMetrics.density * 12
        histogramWidth = resources.displayMetrics.density * 24


    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        textPaint?.color = Color.BLUE
        paint?.let {
            it.style = Paint.Style.STROKE
            it.strokeWidth = 1F
            it.isAntiAlias = true
            it.flags = Paint.ANTI_ALIAS_FLAG
        }
        textPaint?.let {
            it.color = Color.parseColor("#EBEBEB")
            lineY = height - ((it.fontMetrics.bottom - it.fontMetrics.top).apply {
                textHeight = this
            }) - ((textTitleBottomPadding ?: 0F).plus(textTitleTopPadding ?: 0F))

        }
        paint?.let { canvas?.drawLine(0.toFloat(), lineY, width.toFloat(), lineY, it) }
        for (i in strings.indices) {
            drawHistogram(canvas, i, strings[i])
        }

    }

    private fun drawHistogram(canvas: Canvas?, i: Int, text: String) {
        val j = width / strings.size
        val start = i * j

        textPaint?.let {
            it.textSize = resources.displayMetrics.density * 12f
            val textLeft = start + (j - it.measureText(text)) / 2
            it.color = Color.parseColor("#333333")
            canvas?.drawText(
                text,
                textLeft,
                height.toFloat() - textTitleBottomOffset - (textTitleBottomPadding ?: 0F),
                it
            )
        }

        var leftStart = start + (j - histogramWidth * 2 - histogramMargin) / 2
        var rightStart = leftStart + histogramWidth + histogramMargin


        val pathLeft = Path()
        pathLeft.moveTo(leftStart, lineY)
        pathLeft.lineTo(leftStart + histogramWidth, lineY)
        pathLeft.lineTo(leftStart + histogramWidth, textHeight + textDataBottomOffset)
        pathLeft.lineTo(leftStart, textHeight + textDataBottomOffset)
        pathLeft.close()
        setLeftPaint()
        paint?.let { canvas?.drawPath(pathLeft, it) }

        val pathRight = Path()
        pathRight.moveTo(rightStart, lineY)
        pathRight.lineTo(rightStart + histogramWidth, lineY)
        pathRight.lineTo(rightStart + histogramWidth, textHeight + textDataBottomOffset)
        pathRight.lineTo(rightStart, textHeight + textDataBottomOffset)
        pathRight.close()
        setRightPaint()
        paint?.let { canvas?.drawPath(pathRight, it) }

        val percent = "100.00%"
        textPaint?.let {
            it.textSize = histogramWidth / 3 + histogramMargin / 8
            leftStart = leftStart + histogramWidth / 2 - it.measureText(percent) / 2
            it.color = Color.parseColor("#EB4A38")
            canvas?.drawText(percent, leftStart, textHeight, it)
        }
        textPaint?.let {
            it.textSize = histogramWidth / 3 + histogramMargin / 8
            rightStart = rightStart + histogramWidth / 2 - it.measureText(percent) / 2
            it.color = Color.parseColor("#2A83FF")
            canvas?.drawText(percent, rightStart, textHeight, it)
        }

    }

    private fun setRightPaint() {
        paint?.let {
            it.style = Paint.Style.FILL
            it.color = Color.parseColor("#2A83FF")
        }
    }

    private fun setLeftPaint() {
        paint?.let {
            it.style = Paint.Style.FILL
            it.color = Color.parseColor("#EB4A38")
        }
    }
}