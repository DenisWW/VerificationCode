package com.nineone.verificationcode.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewParent

/**
 * created by why
 */
class SimpleTrendChartView : View {
    var paint: Paint = Paint()
    var textPaint: TextPaint = TextPaint()
    var topAndBottomOffset: Float = 4F//设置一个偏移量保证文字绘制的安全区
    var topChartMarginTop: Float = 0F
    var textRightMargin: Float = 0F
    var topAndBottomMargin: Float = 0F
    var leftTextEnd: Float = 0F
    var textHeight: Float = 0F
    private var chartCenterX: Float = 0F
    var pointWidth: Float = 0F
    var ringWidth: Float = 0F
    private var topRectF: RectF = RectF()
    private var bottomRectF: RectF = RectF()
    private var tempRect: Rect = Rect()
    private var tempPath: Path = Path()
    private var isInit: Boolean = false
    private var gestureDetector: GestureDetector? = null
    private var longClick: Boolean = false
    private var bottomColor: Array<String> =
        arrayOf(
            "#bbeB4A38",
            "#77EB4A38",
            "#23EB4A38",
            "#ffffff",
            "#2312B370",
            "#7712B370",
            "#bb12B370"
        )
    private var bottomTitleData: Array<String> =
        arrayOf(
            "百分位",
            "90",
            "80",
            "70",
            "50",
            "30",
            "20"
        )

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initPaint()
        gestureDetector =
            GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
                override fun onLongPress(e: MotionEvent?) {
                    super.onLongPress(e)
                    longClick = true
                }
            })
    }


    private var currentX: Float = -1F
    private fun initPaint() {
        resources.displayMetrics.density.let {
            textRightMargin = it * 4
            topAndBottomMargin = it * 4
            topChartMarginTop = it * 3
            dataPadding = it * 8
            dataMargin = it * 2
            pointWidth = it * 3
            ringWidth = it * 1
        }
    }

    private fun resetTextPaint(color: String) {
        textPaint.reset()
        textPaint.density = resources.displayMetrics.density
        textPaint.textSize = resources.displayMetrics.density * 10
        textPaint.isAntiAlias = true
        textPaint.color = Color.parseColor(color)
    }

    private var bufferCanvas: Canvas? = null
    private var bufferBitmap: Bitmap? = null

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (!isInit) {
            bufferBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            bufferBitmap?.let {
                bufferCanvas = Canvas(it)
            }
            resetTextPaint("#333333")
            val totalChartHeight: Float =
                height - (textPaint.fontMetrics.bottom - textPaint.fontMetrics.top).also {
                    textHeight = it
                } - topChartMarginTop - topAndBottomMargin - topAndBottomOffset * 2
            topRectF.left = (paddingStart + textPaint.measureText("百分位"))
                .also { leftTextEnd = it } + textRightMargin
            topRectF.right = getRealRight() - 1
            topRectF.top = topChartMarginTop
            topRectF.bottom = topChartMarginTop + totalChartHeight * 0.6F

            bottomRectF.left = (paddingStart + textPaint.measureText("百分位")) + textRightMargin
            bottomRectF.right = getRealRight() - 1
            bottomRectF.top = topRectF.bottom + topAndBottomMargin
            bottomRectF.bottom = bottomRectF.top + totalChartHeight * 0.4F

            chartCenterX = topRectF.left + topRectF.width() / 2

            drawLeftText(bufferCanvas)
            drawTopRect(bufferCanvas)
            drawBottomRect(bufferCanvas)
            bufferBitmap?.let {
                canvas?.drawBitmap(it, 0F, 0F, paint)
            }
//            background = BitmapDrawable(resources, bufferBitmap)
            isInit = true
        } else {
            bufferBitmap?.let {
                paint.reset()
                canvas?.drawBitmap(it, 0F, 0F, paint)
            }
            if (currentX >= topRectF.left && currentX <= topRectF.right) {
                paint.reset()
                paint.color = Color.parseColor("#000000")
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = 2F
                canvas?.drawLine(currentX, topRectF.top, currentX, bottomRectF.bottom, paint)
                drawTopData(canvas)
                drawBottomData(canvas)

                drawTopPosition(canvas)
                drawBottomPosition(canvas)
            }
        }
    }

    private fun drawTopPosition(canvas: Canvas?) {
        paint.reset()
        paint.style = Paint.Style.FILL
        paint.isAntiAlias = true
        paint.color = Color.parseColor("#FFFFFF")
        canvas?.drawFilter =
            PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
        canvas?.drawCircle(
            currentX,
            topRectF.top + (pointWidth + ringWidth) * 2,
            (pointWidth + ringWidth),
            paint
        )
        paint.color = Color.parseColor("#2A83FF")
        canvas?.drawCircle(currentX, topRectF.top + (pointWidth + ringWidth) * 2, pointWidth, paint)
    }


    private fun drawBottomPosition(canvas: Canvas?) {
        paint.reset()
        paint.style = Paint.Style.FILL
        paint.isAntiAlias = true
        paint.color = Color.parseColor("#FFFFFF")
        canvas?.drawFilter =
            PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
        canvas?.drawCircle(
            currentX,
            bottomRectF.top + (pointWidth + ringWidth) * 2,
            (pointWidth + ringWidth),
            paint
        )
        paint.color = Color.parseColor("#FF9500")
        canvas?.drawCircle(
            currentX,
            bottomRectF.top + (pointWidth + ringWidth) * 2,
            pointWidth,
            paint
        )
    }


    private var dataPadding = 0F
    private var dataMargin = 0F
    private var hs = arrayListOf(RectF(), RectF(), RectF())
    private fun drawTopData(canvas: Canvas?) {
        val rectF = RectF()
        rectF.top = topRectF.top
        resetTextPaint("#ffffff")
        val data1 = "2018-01-26"
        val data2 = "指数：1000"

        textPaint.getTextBounds(data1, 0, data1.length, tempRect)
        hs[0].top = rectF.top + dataPadding
        hs[0].bottom = hs[0].top + tempRect.height()
        var w = tempRect.width().toFloat()

        textPaint.getTextBounds(data2, 0, data2.length, tempRect)
        hs[1].top = hs[0].bottom + dataMargin + dataMargin / 2
        hs[1].bottom = hs[1].top + tempRect.height()
        w = w.coerceAtLeast(tempRect.width().toFloat()) + dataPadding * 2
        rectF.bottom = hs[1].bottom + dataPadding + dataMargin / 2

        if (currentX > chartCenterX) {
            rectF.left = topRectF.left
            rectF.right = topRectF.left + w
        } else {
            rectF.left = topRectF.right - w
            rectF.right = topRectF.right
        }

        paint.reset()
        paint.color = Color.parseColor("#B3000000")
        paint.style = Paint.Style.FILL
        canvas?.drawRect(rectF, paint)
        canvas?.drawText(
            data1,
            rectF.left + dataPadding,
            hs[0].bottom,
            textPaint
        )
        canvas?.drawText(
            data2,
            rectF.left + dataPadding,
            hs[1].bottom,
            textPaint
        )


    }


    private fun drawBottomData(canvas: Canvas?) {
        val rectF = RectF()
        rectF.top = bottomRectF.top
        resetTextPaint("#ffffff")
        val data1 = "2018-01-26"
        val data2 = "PB：1.84"
        val data3 = "百分位：60.01%"

        Log.e("textPaint", "===" + textPaint.fontMetrics.leading);
        textPaint.getTextBounds(data1, 0, data1.length, tempRect)
        hs[0].top = rectF.top + dataPadding
        hs[0].bottom = hs[0].top + tempRect.height()
        var w = tempRect.width().toFloat()


        textPaint.getTextBounds(data2, 0, data2.length, tempRect)
        hs[1].top = hs[0].bottom + dataMargin + dataMargin
        hs[1].bottom = hs[1].top + tempRect.height()

        w = w.coerceAtLeast(tempRect.width().toFloat())

        textPaint.getTextBounds(data3, 0, data3.length, tempRect)
        hs[2].top = hs[1].bottom + dataMargin + dataMargin / 2
        hs[2].bottom = hs[2].top + tempRect.height()

        w = w.coerceAtLeast(tempRect.width().toFloat()) + dataPadding * 2

        rectF.bottom = hs[2].bottom + dataPadding + dataMargin / 2

        if (currentX > chartCenterX) {
            rectF.left = bottomRectF.left
            rectF.right = bottomRectF.left + w
        } else {
            rectF.left = bottomRectF.right - w
            rectF.right = bottomRectF.right
        }
        paint.reset()
        paint.color = Color.parseColor("#B3000000")
        paint.style = Paint.Style.FILL
        canvas?.drawRect(rectF, paint)

        canvas?.drawText(
            data1,
            rectF.left + dataPadding,
            hs[0].bottom,
            textPaint
        )
        canvas?.drawText(
            data2,
            rectF.left + dataPadding,
            hs[1].bottom,
            textPaint
        )
        canvas?.drawText(
            data3,
            rectF.left + dataPadding,
            hs[2].bottom,
            textPaint
        )


    }


    private fun drawLeftText(canvas: Canvas?) {
        val str = "指数"
        textPaint.getTextBounds(str, 0, str.length, tempRect)
        textPaint.baselineShift
        canvas?.drawText(
            "指数", leftTextEnd - textPaint.measureText("指数"),
//            "指数", leftTextEnd - tempRect.width(),
            tempRect.height().toFloat(), textPaint
        )
    }

    private fun drawTopRect(canvas: Canvas?) {
        drawTopAndLeft(canvas)
        drawBottomAndRight(canvas)
        val rectF = RectF()
        rectF.set(topRectF.left - 1, topRectF.top - 1, topRectF.right + 1, topRectF.bottom + 1)
        paint.reset()
        paint.color = Color.parseColor("#EBEBEB")
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 1F
        canvas?.drawRect(rectF, paint)

        val h = topRectF.height() / 5
        for (i in 0..4) {
            paint.reset()
            paint.pathEffect = DashPathEffect(floatArrayOf(6F, 2F), 0F)
            paint.color = Color.parseColor("#EBEBEB")
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 1F
            canvas?.drawLine(
                topRectF.left, topRectF.top + h * i,
                topRectF.right, topRectF.top + h * i, paint
            )
        }
    }


    private fun drawTopAndLeft(canvas: Canvas?) {
        paint.style = Paint.Style.FILL
        paint.shader = LinearGradient(
            0F, 0F,
            topRectF.bottom, topRectF.right,
            intArrayOf(Color.parseColor("#FFFF5F53"), Color.parseColor("#00FF5F53")),
            null, Shader.TileMode.CLAMP
        )
        val top = Path()
        top.moveTo(topRectF.left, topRectF.top)
        top.lineTo(topRectF.left, topRectF.bottom)
        top.lineTo(topRectF.right, topRectF.top)
        top.close()
        canvas?.drawPath(top, paint)

    }

    private fun drawBottomAndRight(canvas: Canvas?) {
        paint.style = Paint.Style.FILL
        paint.shader = LinearGradient(
            0F, 0F,
            topRectF.bottom, topRectF.right,
            intArrayOf(Color.parseColor("#0035CC8D"), Color.parseColor("#D935CC8D")),
            null, Shader.TileMode.CLAMP
        )
        val top = Path()
        top.moveTo(topRectF.right, topRectF.bottom)
        top.lineTo(topRectF.left, topRectF.bottom)
        top.lineTo(topRectF.right, topRectF.top)
        top.close()
        canvas?.drawPath(top, paint)
    }


    private fun drawBottomRect(canvas: Canvas?) {
        val rectF = RectF()
        var lastBottom = 0F
        var lastHeight = 0F
        val h = bottomRectF.height() / 10
        for (i in bottomColor.indices) {
            val text = bottomTitleData[i]
            paint.reset()
            paint.color = Color.parseColor(bottomColor[i])
            rectF.left = bottomRectF.left
            rectF.right = bottomRectF.right
            if (rectF.top == 0F) rectF.top = bottomRectF.top
            else rectF.top = rectF.bottom
            when (i) {
                3, 4, bottomColor.size - 1 -> {
                    rectF.bottom = rectF.top + h * 2
                }
                else -> {
                    rectF.bottom = rectF.top + h
                }
            }
            canvas?.drawRect(rectF, paint)
            when {
                i == 0 -> {
                    resetTextPaint("#333333")
                    canvas?.drawText(
                        "PB", leftTextEnd - textPaint.measureText("PB"),
                        bottomRectF.top - textHeight, textPaint
                    )
                    canvas?.drawText(
                        text, leftTextEnd - textPaint.measureText(text),
                        bottomRectF.top, textPaint
                    )
                }
                i == 4 -> {
                    resetTextPaint("#666666")
                    textPaint.getTextBounds(text, 0, text.length, tempRect)
                    canvas?.drawText(
                        text, leftTextEnd - textPaint.measureText(text),
                        bottomRectF.top + bottomRectF.height() / 2 + tempRect.height() / 2,
                        textPaint
                    )
                }
                i < 4 -> {
                    resetTextPaint("#666666")
                    textPaint.getTextBounds(text, 0, text.length, tempRect)
                    canvas?.drawText(
                        text, leftTextEnd - textPaint.measureText(text),
                        lastBottom + (tempRect.height() - lastHeight) / 2, textPaint
                    )


                }
                else -> {
                    resetTextPaint("#666666")
                    textPaint.getTextBounds(text, 0, text.length, tempRect)
                    canvas?.drawText(
                        text, leftTextEnd - textPaint.measureText(text),
                        rectF.bottom + (tempRect.height() - rectF.height()) / 2, textPaint
                    )
                }
            }
            lastBottom = rectF.bottom
            lastHeight = rectF.height()
        }
        paint.reset()
        paint.color = Color.parseColor("#FFEBEBEB")
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 1F
        rectF.set(
            bottomRectF.left - 1, bottomRectF.top - 1,
            bottomRectF.right + 1, bottomRectF.bottom + 1
        )
        canvas?.drawRect(rectF, paint)


        canvas?.drawText(
            "2005-05-08", bottomRectF.left,
            bottomRectF.bottom + textHeight + topAndBottomOffset, textPaint
        )
        canvas?.drawText(
            "2022-01-14", bottomRectF.right - textPaint.measureText("2022-01-14"),
            bottomRectF.bottom + textHeight + topAndBottomOffset, textPaint
        )


    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        gestureDetector?.onTouchEvent(event)
        when (event?.action ?: -1 and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                longClick = false
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                currentX = -1F
                invalidate()
                longClick = false
            }
            MotionEvent.ACTION_MOVE -> {
                if (longClick) {
                    val viewParent: ViewParent = parent
                    viewParent.requestDisallowInterceptTouchEvent(true)
                    currentX = event?.x ?: 0F
                    invalidate()
                }
            }
        }
        return true
    }


    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        return onTouchEvent(event)
    }

    private fun getRealRight(): Float {
        return width.toFloat() - paddingEnd
    }

    fun getRealHeight(): Float {
        return height.toFloat() - paddingTop - paddingBottom
    }

}