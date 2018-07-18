package org.youcounter.youcounter.presentation.customviews

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class StolbGraph : View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val rectanglePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bitmapPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    var drawingBitmap: Bitmap? = null
    var drawingCanvas: Canvas? = null
    var drawingRect: Rect? = null

    var curData = listOf(0F, 0F, 0F, 0F, 0F)
        set (value) {
            field = value
            invalidate()
        }


    init {
        backgroundPaint.color = Color.rgb(44, 44, 44)
        backgroundPaint.style = Paint.Style.FILL

        rectanglePaint.color = Color.rgb(67, 160, 71)
        rectanglePaint.style = Paint.Style.FILL
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        drawingBitmap = null
        drawingCanvas = null
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val height = canvas.height
        val width = canvas.width

        if (drawingBitmap == null) {
            drawingBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        }
        val tempBitmap = drawingBitmap ?: return
        tempBitmap.eraseColor(Color.TRANSPARENT)
        if (drawingCanvas == null) {
            drawingCanvas = Canvas(tempBitmap)
        }

        val tempCanvas = drawingCanvas ?: return

        var curX = 0F
        var curY = 0F
        val willAdd = (curData.size * 2 - 1).toDouble()
        val widthOfColumn = (width / willAdd).toDouble()
        val maximumElement = curData.max()
        val resultArray = mutableListOf<Float>()

        if (maximumElement == null) return

        for (i in curData) {
            resultArray.add((i / maximumElement).toFloat())
        }

        for (i in resultArray) {
            tempCanvas.drawRoundRect(curX, height.toFloat() * (1F - i), curX + widthOfColumn.toFloat(), height.toFloat(), 10F, 10F, backgroundPaint)
            curX += (2.0 * widthOfColumn).toFloat()
        }

        if (drawingRect == null) {
            drawingRect = Rect(0, 0, width, height)
        }
        val rect = drawingRect

        canvas.drawBitmap(tempBitmap, rect, rect, bitmapPaint)
    }
}