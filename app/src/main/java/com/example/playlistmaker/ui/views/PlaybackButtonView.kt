package com.example.playlistmaker.ui.views

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.graphics.drawable.toBitmap
import com.example.playlistmaker.R
import kotlin.math.min

class PlaybackButtonView@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0,
) : View(context, attrs, defStyleAttr, defStyleRes){

    private val playImageBitmap: Bitmap?
    private val pauseImageBitmap: Bitmap?
    private var isEnabled: Boolean = false
    private var isPlaying: Boolean = false
    private var imageRect = RectF(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat())

    private var clickListener: OnClickListener? = null


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val size = min(measuredWidth, measuredHeight)
        setMeasuredDimension(size, size)
    }

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.PlaybackButtonView,
            defStyleAttr,
            defStyleRes
        ).apply {
            try {

                playImageBitmap = getDrawable(R.styleable.PlaybackButtonView_playIcon)?.toBitmap()
                pauseImageBitmap = getDrawable(R.styleable.PlaybackButtonView_pauseIcon)?.toBitmap()

            } finally {
                recycle()
            }
        }

        isClickable = true
        isFocusable = true
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        imageRect = RectF(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat())
    }

    override fun performClick(): Boolean {
        super.performClick()
        clickListener?.onClick(this)
        return true
    }

    override fun setOnClickListener(l: OnClickListener?) {
        clickListener = l
    }

    override fun onDraw(canvas: Canvas) {
        if(isEnabled){
            val imageBitmap = if (isPlaying) pauseImageBitmap else playImageBitmap
            imageBitmap?.let {
                canvas.drawBitmap(imageBitmap, null, imageRect, null)
            }
        } else {
            val imageBitmap = playImageBitmap
            imageBitmap?.let {
                canvas.drawBitmap(imageBitmap, null, imageRect, null)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                return true
            }
            MotionEvent.ACTION_UP -> {
                checkPlayer()
                performClick()
                return true
            }
        }

        return super.onTouchEvent(event)
    }

    private fun checkPlayer(){
        isPlaying = !isPlaying
        invalidate()
    }

    fun enableButton(isEnable: Boolean){
        isEnabled = isEnable
    }

    fun isTrackPlaying(isPlaiyng: Boolean){
        this.isPlaying = isPlaiyng
        invalidate()
    }
}