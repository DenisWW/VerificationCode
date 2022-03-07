package com.nineone.verificationcode.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

class SimpleArrowView : View {
    var strokeWidth: Float = 40F;
    var arrowDiffHeight: Float = 50F;
    var arrowWidth: Float = 60F;
    var textRightMargin: Float = 0F;
    var bottomTextOffset: Float = 4F;
    private val anchorWidth = 10F
    private val ringWidth = 5F
    private var textPaint: TextPaint = TextPaint()
    private var paint: Paint = Paint()
    private var arrayInt: IntArray = IntArray(3)
    var anchorY = 0F
    var anchorX = 0F
    private val upBoolean = true

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initPaint()
    }

    private fun initPaint() {
        textPaint = TextPaint()
        textPaint.let {
            it.density = resources.displayMetrics.density
            it.textSize = resources.displayMetrics.density * 10
            it.isAntiAlias = true
            it.color = Color.parseColor("#666666")
        }
        if (upBoolean) {
            arrayInt[0] = Color.parseColor("#00FFFFFF")
            arrayInt[1] = Color.parseColor("#8212B370")
            arrayInt[2] = Color.parseColor("#FF12B370")
        } else {
            arrayInt[0] = Color.parseColor("#FFEB4A38")
            arrayInt[1] = Color.parseColor("#FFF5A097")
            arrayInt[2] = Color.parseColor("#00FFFFFF")
        }

        textRightMargin = resources.displayMetrics.density * 3
        paint.isAntiAlias = true
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return true;
    }


    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val path = Path()
        val text = "9999"
        val dateText = "2018-04-17"
        val textHeight = textPaint.descent().minus(textPaint.ascent())

        anchorX = width.toFloat() - paddingEnd - anchorWidth - ringWidth
        anchorY =
            height.toFloat() - paddingBottom - anchorWidth - ringWidth - strokeWidth / 2 - arrowDiffHeight / 2 - bottomTextOffset * 2 - textHeight
        if (upBoolean)
            anchorY =
                paddingTop.toFloat() + anchorWidth + ringWidth + strokeWidth / 2 + arrowDiffHeight / 2

        val rectF: RectF = RectF(
            paddingStart.toFloat().plus(textPaint.measureText(text)) + textRightMargin,
            paddingTop.toFloat(), width.toFloat() - paddingEnd,
            height.toFloat() - paddingBottom - bottomTextOffset * 2 - textHeight
        )
        val rect = Rect()
        textPaint.getTextBounds(text, 0, text.length, rect)

        textPaint.let {
            canvas?.drawText(
                text, rectF.left - textRightMargin - it.measureText(text), textHeight, it
            )
            canvas?.drawText(
                text, rectF.left - textRightMargin - it.measureText(text), rectF.bottom, it
            )
            canvas?.drawText(
                dateText, rectF.left, rectF.bottom + bottomTextOffset + textHeight, it
            )
            canvas?.drawText(
                dateText,
                rectF.right - it.measureText(dateText),
                rectF.bottom + bottomTextOffset + textHeight,
                it
            )
        }


        path.addRect(rectF, Path.Direction.CCW)
        paint.shader = LinearGradient(
            rectF.left, rectF.top, rectF.left, rectF.bottom, arrayInt, null, Shader.TileMode.CLAMP
        )
//        canvas?.drawPath(path, paint)
        canvas?.drawRect(rectF, paint)


        val arrowRectF = RectF()
        arrowRectF.top = anchorY - anchorWidth - ringWidth
        arrowRectF.right = anchorX
        arrowRectF.left = rectF.left
        arrowRectF.bottom = rectF.bottom
        paint.reset()
        paint.shader = null
        paint.isAntiAlias = true
        val startX = rectF.left + anchorWidth + ringWidth

        var startY = rectF.top + anchorWidth + ringWidth
        if (upBoolean) startY = rectF.bottom - anchorWidth - ringWidth

        paint.color = Color.parseColor("#FFFFFF")
        canvas?.drawCircle(anchorX, anchorY, anchorWidth + ringWidth, paint)
        canvas?.drawCircle(startX, startY, anchorWidth + ringWidth, paint)
        paint.color = Color.parseColor("#033333")
        canvas?.drawCircle(anchorX, anchorY, anchorWidth, paint)
        canvas?.drawCircle(startX, startY, anchorWidth, paint)

        path.reset();
        paint.isAntiAlias = true
        paint.strokeWidth = strokeWidth
        paint.shader = LinearGradient(
            arrowRectF.left,
            arrowRectF.top,
            arrowRectF.right,
            arrowRectF.top,
            Color.parseColor("#0A2A83FF"),
            Color.parseColor("#2A83FF"),
            Shader.TileMode.CLAMP
        )
        paint.style = Paint.Style.STROKE
        val diffH = (arrowDiffHeight + paint.strokeWidth) / 2;
//        path.moveTo(arrowRectF.left, arrowRectF.bottom - paint.strokeWidth);
        path.moveTo(startX, startY)
        val lineEndX = anchorX - arrowWidth - anchorWidth - ringWidth
        val centerX = startX + arrowRectF.width() / 3
        path.quadTo(centerX, anchorY, lineEndX, anchorY)

        canvas?.drawPath(path, paint)
        val pathMeasure = PathMeasure(path, false)

        path.reset()
//        arrowDiffHeight / 2 + paint.strokeWidth / 2
        path.moveTo(anchorX - anchorWidth - ringWidth, anchorY)
        path.lineTo(lineEndX, anchorY - diffH)
        path.lineTo(lineEndX, anchorY + diffH)
        path.close()
        paint.style = Paint.Style.FILL
        paint.color = Color.parseColor("#2A83FF")
        canvas?.drawPath(path, paint)
        drawStartText(canvas, startX, startY, centerX, pathMeasure)

    }

    private fun drawStartText(
        canvas: Canvas?,
        startX: Float,
        startY: Float,
        centerX: Float,
        pathMeasure: PathMeasure
    ) {
        val dataText = "PB值等于当前的PB"
        val leftAndRight = 4 * resources.displayMetrics.density
        val topAndBottom = 2 * resources.displayMetrics.density
        val r = 2 * resources.displayMetrics.density
        val dataRight =
            startX + textPaint.measureText(dataText) + leftAndRight * 2 - ringWidth - anchorWidth
        val triangleWidth = 20F
        val triangleHeight = 10F
        val diffY = anchorWidth + ringWidth + triangleHeight
        val rect = Rect()
        textPaint.getTextBounds(dataText, 0, dataText.length, rect)
        val textHeight = textPaint.descent() - textPaint.ascent() + topAndBottom * 2
//        val textHeight = rect.height() + topAndBottom * 2
        var dataBottom = startY + textHeight + diffY
        val path = Path()

        var rectF = RectF(
            startX - ringWidth - anchorWidth,
            startY + diffY,
            dataRight,
            dataBottom
        )

        path.addRoundRect(rectF, r, r, Path.Direction.CCW)
        path.moveTo(rectF.left + r, rectF.top )
        path.lineTo(rectF.left + r + triangleWidth, rectF.top )
        path.lineTo(rectF.left + r + triangleWidth / 2, rectF.top- triangleHeight)

        if (upBoolean) {
            path.reset()
            dataBottom = startY - textHeight - diffY
            rectF = RectF(
                startX - ringWidth - anchorWidth, dataBottom,
                dataRight, startY - diffY
            )
            path.addRoundRect(rectF, r, r, Path.Direction.CCW)
            path.moveTo(rectF.left + r, rectF.bottom)
            path.lineTo(rectF.left + r + triangleWidth, rectF.bottom)
            path.lineTo(rectF.left + r + triangleWidth / 2, rectF.bottom + triangleHeight)
        }

        path.close()
        resetPaint(Color.parseColor("#88000000"))
        canvas?.drawPath(path, paint)
        textPaint.color = Color.parseColor("#FFFFFF")
        var textStartY = rectF.bottom - textPaint.descent() - topAndBottom
        canvas?.drawText(
            dataText, rectF.left + leftAndRight, textStartY, textPaint
        )


        canvas?.drawCircle(centerX, anchorY, 10F, paint)
        canvas?.drawLine(centerX, anchorY, anchorX, anchorY, paint)
        canvas?.drawLine(startX, startY, anchorX, anchorY, paint)
        canvas?.drawLine(startX, startY, centerX, anchorY, paint)
//        canvas?.drawCircle(centerX, (startY - anchorY) / 4 + anchorY, 10F, paint)

        val array = FloatArray(2)
        pathMeasure.getPosTan(pathMeasure.length * 2 / 5, array, null)
        canvas?.drawCircle(array[0], array[1], 10F, paint)
        val dataTextCenter = "PB值等于当前的PB"
        textPaint.getTextBounds(dataTextCenter, 0, dataTextCenter.length, rect)
        rectF = RectF(
            array[0] - textPaint.measureText(dataTextCenter) - leftAndRight * 2,
            array[1],
            array[0],
            array[1] + textHeight
        )
        if (upBoolean) {
            rectF = RectF(
                array[0] - textPaint.measureText(dataTextCenter) - leftAndRight * 2,
                array[1] - textHeight,
                array[0],
                array[1]
            )

        }

        path.reset()
        path.addRoundRect(rectF, r, r, Path.Direction.CCW)
        path.moveTo(rectF.right + triangleHeight, rectF.centerY())
        path.lineTo(rectF.right, rectF.centerY() + triangleWidth / 2)
        path.lineTo(rectF.right, rectF.centerY() - triangleWidth / 2)
        path.close()
        resetPaint(Color.parseColor("#88000000"))
        canvas?.drawPath(path, paint)
        textPaint.color = Color.parseColor("#FFFFFF")
        textStartY = rectF.bottom - textPaint.descent() - topAndBottom
        canvas?.drawText(
            dataTextCenter, rectF.left + leftAndRight, textStartY, textPaint
        )
    }

    private fun resetPaint(color: Int) {
        paint.reset()
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
        paint.color = color
    }
}