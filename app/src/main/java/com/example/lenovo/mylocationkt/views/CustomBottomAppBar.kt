package com.example.lenovo.mylocationkt.views

import android.content.Context
import android.graphics.*
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import com.example.lenovo.mylocationkt.R
import com.example.lenovo.mylocationkt.pxFromDp

// Constants
private val COLOR_SOLID = R.color.colorPrimary
private val RADIUS_CIRCLE: Float = 35f
private val RADIUS_CORNER: Float = 5f

/**
 * Navigation bar
 */
class CustomBottomAppBar : View {

    /** context */
    lateinit var mContext: Context
    /** fill color */
    private lateinit var fillPaint: Paint
    /** path */
    private lateinit var path: Path

    /**
     * Constructor
     * @param context context
     */
    constructor(context: Context) : super(context) {
        init(context)
    }

    /**
     * Constructor
     * @param context context
     * @param attrs attributes
     */
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    /**
     * Constructor
     * @param context context
     * @param attrs attributes
     * @param defStyleAttr style attributes
     */
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    /**
     * Initialize
     * @param context context
     */
    private fun init(context: Context) {
        this.mContext = context
        fillPaint = Paint()
        fillPaint.color = ContextCompat.getColor(context, COLOR_SOLID)
        fillPaint.style = Paint.Style.FILL
        fillPaint.strokeWidth = 3f
        fillPaint.isAntiAlias = true

        setBackgroundColor(Color.TRANSPARENT)
        path = Path()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val radiusCircle: Int = pxFromDp(RADIUS_CIRCLE, context).toInt()
        val radiusCorner: Int = pxFromDp(RADIUS_CORNER, context).toInt()
        val posCenterX = (width / 2.0).toInt()

        path.reset()
        path.moveTo(0f, 0f)
        path.lineTo((posCenterX - radiusCircle - radiusCorner).toFloat(), 0f)
        path.arcTo(getUniformRectF(posCenterX - radiusCircle - radiusCorner, radiusCorner, radiusCorner), 270f, 90f)
        path.arcTo(getUniformRectF(posCenterX, 0, radiusCircle), 185f, -170f)
        path.arcTo(getUniformRectF(posCenterX + radiusCircle + radiusCorner, radiusCorner, radiusCorner), 180f, 90f)
        path.lineTo(width.toFloat(), 0f)
        path.lineTo(width.toFloat(), height.toFloat())
        path.lineTo(0f, height.toFloat())
        path.close()
    }

    /**
     * Calculates the uniform oval (= circle) around a given center point with the given radius
     *
     * @param centerX x coordinate center point
     * @param centerY y coordinate center point
     * @param radius  half side length
     * @return rect
     */
    private fun getUniformRectF(centerX: Int, centerY: Int, radius: Int): RectF {
        return RectF(
            (centerX - radius).toFloat(),
            (centerY - radius).toFloat(),
            (centerX + radius).toFloat(),
            (centerY + radius).toFloat()
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawPath(path, fillPaint)
    }
}