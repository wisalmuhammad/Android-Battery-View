package com.wisal.android.views.batteryview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.annotation.CheckResult
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat



class BatteryView @JvmOverloads constructor(ctx: Context, attributeSet: AttributeSet? = null, styleAttr: Int = 0) : View(ctx,attributeSet,styleAttr) {

    private val TAG = "BatteryView"

    companion object {
        const val DEFAULT_BORDER_COLOR = Color.GRAY
        const val DEFAULT_STROKE_WIDTH = 2f
        const val DEFAULT_MARGIN = 4
        const val DEFAULT_BATTERY_LEVEL = 30
        const val DEFAULT_BATTERY_WARNING_LEVEL = 15
    }

    private val headRect = Rect()
    private val levelRect = Rect()
    private val borderRect = Rect()

    private val borderPaint: Paint = Paint().apply {
        flags = Paint.ANTI_ALIAS_FLAG
        style = Paint.Style.STROKE
        strokeWidth = DEFAULT_STROKE_WIDTH
    }
    private val textPaint: Paint = Paint().apply {
        flags = Paint.ANTI_ALIAS_FLAG
        textAlign = Paint.Align.CENTER
    }
    private val headPaint: Paint = Paint().apply {
        flags = Paint.ANTI_ALIAS_FLAG
        style = Paint.Style.FILL
        color = DEFAULT_BORDER_COLOR
        strokeWidth = DEFAULT_STROKE_WIDTH
    }
    private val batteryLevelPaint: Paint = Paint().apply {
        flags = Paint.ANTI_ALIAS_FLAG
        style = Paint.Style.FILL
        color = Color.GREEN
    }


    private var desiredWidth: Float = dp2px(24f)
    private var desiredHeight: Float = desiredWidth * 0.45f

    var isCharging: Boolean = false
        set(value) {
            field = value
            invalidate()
        }

    var warningLevel: Int = DEFAULT_BATTERY_WARNING_LEVEL
        set(value) {
            field = when {
                value > 100 -> 100
                value < 0 -> 0
                else -> value
            }
            invalidate()
        }
    var batteryLevel: Int = DEFAULT_BATTERY_LEVEL
        @CheckResult
        get
        set(value) {
            field = when {
                value < 0 -> 0
                value > 100 -> 100
                else -> value
            }
            if (field <= warningLevel) {
                batteryLevelPaint.color = Color.RED
            } else {
                batteryLevelPaint.color = Color.GREEN
            }
            invalidate()
        }


    private var defaultMargin: Int = DEFAULT_MARGIN
    private val borderStroke: Float

    init {
        val typedArray = ctx.obtainStyledAttributes(
            attributeSet,
            R.styleable.BatteryView,
            0,
            0
        )

        borderStroke = typedArray.getDimension(R.styleable.BatteryView_outer_border_stroke_width,DEFAULT_STROKE_WIDTH)
        isCharging = typedArray.getBoolean(R.styleable.BatteryView_charging,false)
        batteryLevel = typedArray.getInt(R.styleable.BatteryView_battery_level, DEFAULT_BATTERY_LEVEL)
        warningLevel = typedArray.getInteger(R.styleable.BatteryView_warning_level, DEFAULT_BATTERY_WARNING_LEVEL)
        borderPaint.color = typedArray.getColor(R.styleable.BatteryView_outer_border_color, DEFAULT_BORDER_COLOR)
        typedArray.recycle()

        borderPaint.strokeWidth = dp2px(borderStroke)
        defaultMargin = (borderPaint.strokeWidth / 2).toInt()

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val measuredWidth = MeasureSpec.getSize(widthMeasureSpec)
        val measuredHeight = MeasureSpec.getSize(heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        val desiredWidth: Int = (dp2px(DEFAULT_STROKE_WIDTH) + desiredWidth + Math.max(paddingBottom + paddingTop, paddingLeft + paddingRight)).toInt()
        val desiredHeight: Int = (dp2px(DEFAULT_STROKE_WIDTH) + desiredHeight + Math.max(paddingBottom + paddingTop, paddingLeft + paddingRight)).toInt()

        val finalWidth: Int = when (widthMode) {
            MeasureSpec.EXACTLY -> measuredWidth
            MeasureSpec.AT_MOST -> Math.min(desiredWidth, measuredWidth)
            else -> desiredWidth
        }

        val finalHeight: Int = when (heightMode) {
            MeasureSpec.EXACTLY -> measuredHeight
            MeasureSpec.AT_MOST -> Math.min(desiredHeight, measuredHeight)
            else -> desiredHeight
        }

        val widthWithoutPadding = finalWidth - paddingLeft - paddingRight
        val heightWithoutPadding = finalHeight - paddingTop - paddingBottom

        setMeasuredDimension(widthWithoutPadding, heightWithoutPadding)

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {

        val headWidth = borderPaint.strokeWidth
        textPaint.textSize = h * .5f
        borderRect.set(
            defaultMargin,
            defaultMargin,
            (w - 2 * defaultMargin - headWidth).toInt(),
            (h - defaultMargin)
        )


        levelRect.set(
            borderRect.left + borderStroke.toInt() + defaultMargin,
            borderRect.top + borderStroke.toInt() + defaultMargin,
            ((borderRect.right - borderStroke.toInt() - defaultMargin) * (this.batteryLevel.toDouble() / 100.toDouble())).toInt() ,
            borderRect.bottom - borderStroke.toInt() - defaultMargin,
        )


        headRect.set(
            borderRect.right + (2 * defaultMargin).toInt(),
            borderRect.top + h / 5,
            (borderRect.right + (2 * defaultMargin) + headWidth).toInt(),
            borderRect.bottom - h / 5
        )
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        drawOuterBorder(canvas)
        drawLevel(canvas)
        drawHead(canvas)

        if(isCharging) {
            drawChargingIcon(canvas)
        } else {
            drawBatteryStatus(canvas)
        }
    }


    private fun drawOuterBorder(canvas: Canvas?) {
        val rectR = RectF(borderRect)
        canvas?.drawRoundRect(
            rectR,
            5f,
            5f,
            borderPaint
        )
    }

    private fun drawLevel(canvas: Canvas?) {

        batteryLevelPaint.color = if (batteryLevel <= warningLevel) Color.RED
        else Color.GREEN

        if(batteryLevel == 0) batteryLevel = defaultMargin
        levelRect.right = ((borderRect.right - borderStroke.toInt() - defaultMargin) * (this.batteryLevel.toDouble() / 100.toDouble())).toInt()
        val rectF = RectF(levelRect)
        canvas?.drawRoundRect(rectF,5f,5f,batteryLevelPaint)
    }

    private fun drawHead(canvas: Canvas?) {
        val rectF = RectF(headRect)
        canvas?.drawRoundRect(
            rectF,
            25f,
            25f,
            headPaint
        )
    }

    private fun drawChargingIcon(canvas: Canvas?) {
        val vectorDrawable = VectorDrawableCompat.create(context.resources,R.drawable.ic_charging_bolt,null)
        vectorDrawable?.setBounds(
            borderRect.left + borderRect.width() / 4,
            borderRect.top + borderRect.height() / 4,
            borderRect.right - borderRect.width() / 4,
            borderRect.bottom - borderRect.height() / 4
        )
        vectorDrawable?.setColorFilter(Color.DKGRAY, PorterDuff.Mode.SRC_IN)
        vectorDrawable?.draw(canvas!!)
    }

    private fun drawBatteryStatus(canvas: Canvas?) {
        if(batteryLevel <= warningLevel) {
            val vectorDrawable = VectorDrawableCompat.create(context.resources,R.drawable.ic_warning,null)
            vectorDrawable?.setBounds(
                borderRect.left + borderRect.width() / 4,
                borderRect.top + borderRect.height() / 7,
                borderRect.right - borderRect.width() / 4,
                borderRect.bottom - borderRect.height() / 7
            )
            vectorDrawable?.setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN)
            vectorDrawable?.draw(canvas!!)
        } else {
            drawBatteryLevelText(canvas)
        }
    }

    private fun drawBatteryLevelText(canvas: Canvas?) {
        val level = batteryLevel.toString()
        val xPos = canvas!!.width / 2
        val yPos = (canvas.height / 2 - (textPaint.descent() + textPaint.ascent()) / 2).toInt()
        canvas.drawText(
            "$level%",
            xPos.toFloat(),
            yPos.toFloat(),
            textPaint
        )
    }

}

fun View.dp2px(dp: Float): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        resources.displayMetrics
    )
}