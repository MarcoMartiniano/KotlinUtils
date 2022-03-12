package com.example.kotlinutils.customViews.components

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.core.content.ContextCompat
import com.example.kotlinutils.R


open class CustomSwitchButton : View {

    constructor(context: Context?) : this(context, null)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context, attrs, defStyleAttr)
    }

    protected open fun initView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        initAttributes(attrs)
        initButtonState()
        initPaint()
    }

    private fun initAttributes(attrs: AttributeSet?) {
        val array = context.obtainStyledAttributes(attrs, R.styleable.PxSwitchButton)
        thumbBgShadowColor =
            array.getColor(R.styleable.PxSwitchButton_thumbBgShadowColor, thumbBgShadowColor)
        thumbOnBgColor = array.getColor(R.styleable.PxSwitchButton_thumbOnBgColor, thumbOnBgColor)
        thumbOffBgColor = array.getColor(R.styleable.PxSwitchButton_thumbOffBgColor, -1)
        trackOffTransitBgColor =
            array.getColor(
                R.styleable.PxSwitchButton_trackOffTransitBgColor,
                trackOffTransitBgColor
            )
        trackOnBgColor = array.getColor(R.styleable.PxSwitchButton_trackOnBgColor, trackOnBgColor)
        trackOffBgColor =
            array.getColor(R.styleable.PxSwitchButton_trackOffBgColor, trackOffBgColor)
        if (thumbOffBgColor == -1) {
            thumbOffBgColor = thumbOnBgColor
        }
        thumbRadius = array.getDimensionPixelOffset(
            R.styleable.PxSwitchButton_thumbRadius,
            DEFAULT_THUMB_RADIUS
        )
        trackWidth =
            array.getDimensionPixelOffset(
                R.styleable.PxSwitchButton_trackWidth,
                DEFAULT_TRACK_WIDTH
            )
        trackHeight =
            array.getDimensionPixelOffset(
                R.styleable.PxSwitchButton_trackHeight,
                DEFAULT_TRACK_HEIGHT
            )
        trackBgRadius =
            array.getDimensionPixelOffset(
                R.styleable.PxSwitchButton_trackBgRadius,
                -1
            )
        if (trackBgRadius == -1) {
            trackBgRadius = trackHeight / 2
        }

        thumbAnimatorDuration =
            array.getInt(R.styleable.PxSwitchButton_thumbAnimatorDuration, thumbAnimatorDuration)
        thumbSize = thumbRadius * 2

        thumbShadowDx = array.getInt(R.styleable.PxSwitchButton_thumbShadowDx, thumbShadowDx)
        thumbShadowDy = array.getInt(R.styleable.PxSwitchButton_thumbShadowDy, thumbShadowDy)
        thumbShadowRadius =
            array.getInt(R.styleable.PxSwitchButton_thumbShadowRadius, thumbShadowRadius)

        isOpened = array.getBoolean(R.styleable.PxSwitchButton_isOpen, isOpened)
        isEnableThumbShadow = array.getBoolean(R.styleable.PxSwitchButton_isEnableThumbShadow, true)
        isOpenedLast = !isOpened
        array.recycle()
    }

    private fun initButtonState() {
        if (isOpened) {
            bgAlpha = 255
            animatedFraction = 0f
            thumbOffsetParent = 1.0f
        } else {
            bgAlpha = 0
            animatedFraction = 1f
            thumbOffsetParent = 0f
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val defaultWidth = trackWidth
        val defaultHeight = calculateHeight()
        val width = measureSize(widthMeasureSpec, defaultWidth)
        val height = measureSize(heightMeasureSpec, defaultHeight)
        setMeasuredDimension(width, height)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        initPath()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawFilter =
            PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
        canvas?.let { onDrawToggleTrack(it) }
        canvas?.let { onDrawToggleThumbShadow(it) }
        canvas?.let { onDrawToggleThumb(it) }
    }

    protected open fun onDrawToggleTrack(canvas: Canvas) {
        canvas.save()
        canvas.drawPath(trackPath, trackOffPaint)
        trackOnPaint.alpha = bgAlpha
        canvas.drawPath(trackPath, trackOnPaint)
        canvas.restore()
    }

    protected open fun onDrawToggleThumbShadow(canvas: Canvas) {
        if (!isEnableThumbShadow) {
            return
        }
        thumbShadowPath.reset()
        val alpha = animatedFraction * 255
        val showDx = if (isOpenedLast) {
            thumbShadowDx.toFloat()
        } else {
            -thumbShadowDx.toFloat()
        }
        val centerX: Float = thumbOffCenterX + thumbOffsetParent * thumbTotalOffset
        thumbShadowPaint.alpha = alpha.toInt()
        thumbShadowPaint.setShadowLayer(
            thumbShadowRadius.toFloat(),
            showDx,
            thumbShadowDy.toFloat(),
            thumbBgShadowColor
        )
        thumbShadowPath.addCircle(centerX, thumbCenterY, thumbShadowSize * 0.5f, Path.Direction.CW)
        canvas.save()
        canvas.drawPath(thumbShadowPath, thumbShadowPaint)
        canvas.restore()
    }

    protected open fun onDrawToggleThumb(canvas: Canvas) {
        thumbPath.reset()
        val centerX = thumbOffCenterX + thumbOffsetParent * thumbTotalOffset
        thumbPath.addCircle(centerX, thumbCenterY, thumbSize * 0.5f, Path.Direction.CW)

        canvas.save()
        canvas.drawPath(thumbPath, thumbOffBgPaint)
        thumbOnBgPaint.alpha = bgAlpha
        canvas.drawPath(thumbPath, thumbOnBgPaint)
        canvas.restore()
    }

    private fun initPath() {
        initTrackPath()
        initThumbConfig()
    }

    protected open fun initTrackPath() {
        trackPath.reset()
        trackRectF.left = (width - trackWidth.toFloat()) / 2
        trackRectF.right = this.trackRectF.left + trackWidth
        trackRectF.top = (height - trackHeight.toFloat()) / 2
        trackRectF.bottom = this.trackRectF.top + trackHeight
        trackPath.addRoundRect(
            this.trackRectF,
            trackBgRadius.toFloat(),
            trackBgRadius.toFloat(),
            Path.Direction.CW
        )
    }

    protected open fun initThumbConfig() {
        var padding = (height - thumbSize) * 0.5f
        if (isEnableThumbShadow) {
            padding -= (thumbShadowSize / 2)
        }
        thumbOffCenterX = padding + thumbSize * 0.5f
        thumbOnCenterX = width - padding - thumbSize * 0.5f
        thumbCenterY = height * 0.5f
        thumbTotalOffset = width - padding - thumbOffCenterX - thumbSize * 0.5f
        thumbShadowSize = (thumbSize - thumbShadowRadius * 3)
    }

    private fun measureSize(measureSpec: Int, defaultSize: Int): Int {
        var result: Int
        val mode = MeasureSpec.getMode(measureSpec)
        val size = MeasureSpec.getSize(measureSpec)
        if (mode == MeasureSpec.EXACTLY) {
            result = size
        } else {
            result = defaultSize
            if (mode == MeasureSpec.AT_MOST) result = result.coerceAtMost(size)
        }
        return result
    }

    private fun calculateHeight(): Int {
        var height = if (thumbSize > trackHeight) {
            thumbSize
        } else if (thumbSize + thumbShadowSize > trackHeight) {
            thumbSize
        } else {
            trackHeight
        }
        if (isEnableThumbShadow && height < thumbSize + thumbShadowSize) {
            height = +thumbShadowSize
        }
        return height
    }

    protected open fun initPaint() {
        initTrackPaint()
        initThumbPaint()
    }

    protected open fun initTrackPaint() {
        trackOnPaint.style = Paint.Style.FILL
        trackOnPaint.strokeJoin = Paint.Join.ROUND
        trackOnPaint.strokeCap = Paint.Cap.ROUND
        trackOnPaint.color = trackOnBgColor
        trackOnPaint.isAntiAlias = true
        trackOnPaint.isDither = true

        trackOffPaint.style = Paint.Style.FILL
        trackOffPaint.strokeJoin = Paint.Join.ROUND
        trackOffPaint.strokeCap = Paint.Cap.ROUND
        trackOffPaint.color = trackOffBgColor
        trackOffPaint.isAntiAlias = true
        trackOffPaint.isDither = true
    }

    private fun initThumbPaint() {
        thumbOnBgPaint.isAntiAlias = true
        thumbOnBgPaint.isDither = true
        thumbOnBgPaint.style = Paint.Style.FILL
        thumbOnBgPaint.strokeJoin = Paint.Join.ROUND
        thumbOnBgPaint.strokeCap = Paint.Cap.ROUND
        thumbOnBgPaint.color = thumbOnBgColor

        thumbOffBgPaint.isAntiAlias = true
        thumbOffBgPaint.isDither = true
        thumbOffBgPaint.style = Paint.Style.FILL
        thumbOffBgPaint.strokeJoin = Paint.Join.ROUND
        thumbOffBgPaint.strokeCap = Paint.Cap.ROUND
        thumbOffBgPaint.color = thumbOffBgColor

        thumbShadowPaint.isAntiAlias = true
        thumbShadowPaint.isDither = true
        thumbShadowPaint.style = Paint.Style.FILL
        thumbShadowPaint.strokeJoin = Paint.Join.ROUND
        thumbShadowPaint.strokeCap = Paint.Cap.ROUND
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (thumbOffsetParent != 0f && thumbOffsetParent != 1f) {
            return true
        }
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                return true
            }
            MotionEvent.ACTION_UP -> {
                if (toggleSwitch(!isOpened)) {
                    toggleListener?.onSwitchToggleChange(isOpened, this)
                    callOnClick()
                }
            }
        }
        return super.onTouchEvent(event)
    }

    open fun toggleSwitch(isOpen: Boolean): Boolean {
        if (this.isOpened == isOpen) {
            return false
        }
        this.isOpenedLast = isOpened
        this.isOpened = isOpen
        startThumbAnimator()
        return true
    }

    protected open fun startThumbAnimator() {
        if (!isOpenedLast) {
            //off->on
            thumbAnimator.setFloatValues(0f, 1f)
        } else {
            //on->off
            thumbAnimator.setFloatValues(1f, 0f)
        }
        thumbAnimator.start()
    }

    @ColorInt
    var thumbBgShadowColor: Int = Color.BLACK
    var thumbShadowDy: Int = 14
    var thumbShadowDx: Int = 8
    var thumbShadowRadius: Int = 16

    @ColorInt
    var thumbOnBgColor: Int = ContextCompat.getColor(context, R.color.background_light_level_2)

    @ColorInt
    var thumbOffBgColor: Int = ContextCompat.getColor(context, R.color.background_light_level_2)

    @ColorInt
    var trackOffTransitBgColor: Int =
        ContextCompat.getColor(context, R.color.background_light_level_2)

    @ColorInt
    var trackOnBgColor: Int = ContextCompat.getColor(context, R.color.primary_light)

    @ColorInt
    var trackOffBgColor: Int = ContextCompat.getColor(context, R.color.neutral_medium)

    @Dimension
    var thumbRadius: Int = 0

    @Dimension
    var thumbSize: Int = thumbRadius * 2

    @Dimension
    var trackWidth: Int = 0

    @Dimension
    var trackHeight: Int = 0

    @Dimension
    var trackBgRadius: Int = 0

    var isOpened: Boolean = false
    var isOpenedLast: Boolean = !isOpened
    var isEnableThumbShadow: Boolean = false
    var animatedFraction: Float = 1f
    var toggleListener: SwitchChangeListener? = null

    protected open var trackOnPaint: Paint = Paint()
    protected open var trackOffPaint: Paint = Paint()
    protected open var thumbShadowPaint = Paint()
    protected open var thumbOnBgPaint: Paint = Paint()
    protected open var thumbOffBgPaint: Paint = Paint()
    protected open var trackPath: Path = Path()
    protected open var thumbPath: Path = Path()
    protected open var thumbShadowPath: Path = Path()
    protected open var trackRectF = RectF()
    protected open var thumbOffCenterX: Float = 0f
    protected open var thumbOnCenterX: Float = 0f
    protected open var thumbCenterY: Float = 0f
    protected open var thumbTotalOffset: Float = 0f
    protected open var thumbOffsetParent: Float = 0f
    protected open var thumbAnimatorDuration: Int = 300
    protected open var thumbShadowSize: Int = 0
    protected open var bgAlpha: Int = 0
    protected open val thumbAnimator: ValueAnimator by lazy {
        newThumbAnimator(isNew = true)
    }

    fun setSwitchBackError(thumbAnimatorDuration: Int = 300, isSwitchOn: Boolean = false) {
        newThumbAnimator(thumbAnimatorDuration = thumbAnimatorDuration, isSwitchOn = isSwitchOn)
        startThumbAnimator()
    }

    fun setSwitchInitialValor(isSwitchOn: Boolean = true) {
        when (isSwitchOn) {
            true -> {
                isOpened = true
                isOpenedLast = false
            }
            false -> {
                isOpened = false
                isOpenedLast = true
            }
        }
    }

    protected open fun newThumbAnimator(
        thumbAnimatorDuration: Int = 300,
        isSwitchOn: Boolean = false,
        isNew: Boolean = false
    ): ValueAnimator {
        val valueAnimator = ValueAnimator.ofFloat(1f, 0f)
        if (!isNew) {
            when (isSwitchOn) {
                true -> {
                    isOpened = true
                    isOpenedLast = false
                }
                false -> {
                    isOpened = false
                    isOpenedLast = true
                }
            }
        }

        valueAnimator.duration = thumbAnimatorDuration.toLong()
        valueAnimator.addUpdateListener {
            animatedFraction = it.animatedFraction
            thumbOffsetParent = it.animatedValue as Float
            bgAlpha = ((it.animatedValue as Float) * 255).toInt()
            postInvalidate()
        }
        return valueAnimator
    }

    fun setSwitchChangeListener(listener: SwitchChangeListener): CustomSwitchButton {
        this.toggleListener = listener
        return this
    }

    companion object {
        private const val DEFAULT_THUMB_RADIUS: Int = 42
        private const val DEFAULT_TRACK_WIDTH: Int = 150
        private const val DEFAULT_TRACK_HEIGHT: Int = 90

        interface SwitchChangeListener {
            fun onSwitchToggleChange(isOpen: Boolean, button: CustomSwitchButton)
        }
    }
}