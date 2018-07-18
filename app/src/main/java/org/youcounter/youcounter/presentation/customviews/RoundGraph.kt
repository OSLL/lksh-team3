package org.youcounter.youcounter.presentation.customviews

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View


class RoundGraph : View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val rectanglePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bitmapPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    var drawingBitmap: Bitmap? = null
    var drawingCanvas: Canvas? = null
    var drawingRect: Rect? = null

    var curPercent = 0.0
        set (value) {
            field = Math.min(Math.max(value, 0.0), 1.0)
            invalidate()
        }

    private val circleStroke = 50.0f

    init {
        circlePaint.color = Color.rgb(235, 235, 235)
        circlePaint.strokeWidth = circleStroke
        circlePaint.style = Paint.Style.STROKE

        rectanglePaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP)
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

        val cX = width / 2F
        val cY = height / 2F

        val radius = (Math.min(width, height) / 2F) - (circleStroke / 2F)

        tempCanvas.drawCircle(cX, cY, radius, circlePaint)

        val third = Math.min(width, height) * curPercent

        tempCanvas.drawRect(0F, (height - third).toFloat(), width.toFloat(), height.toFloat(), rectanglePaint)

        if (drawingRect == null) {
            drawingRect = Rect(0, 0, width, height)
        }
        val rect = drawingRect

        canvas.drawBitmap(tempBitmap, rect, rect, bitmapPaint)
    }

}