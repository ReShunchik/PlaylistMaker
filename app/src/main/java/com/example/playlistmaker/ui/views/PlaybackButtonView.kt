package com.example.playlistmaker.ui.views

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF
import android.media.MediaPlayer
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.playlistmaker.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.getKoin
import java.text.SimpleDateFormat
import kotlin.math.min

class PlaybackButtonView@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0,
) : View(context, attrs, defStyleAttr, defStyleRes){

    private val playImageBitmap: Bitmap?
    private val pauseImageBitmap: Bitmap?
    private lateinit var mediaPlayer: MediaPlayer
    private var isTrackPlaying: Boolean = false
    private var imageRect = RectF(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat())

    private var timerJob: Job? = null
    private val coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private val trackTimeLiveData = MutableLiveData<String>(TIME_DEFAULT)
    fun observeTrackTime(): LiveData<String> = trackTimeLiveData

    private val dateFormat: SimpleDateFormat = getKoin().get()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val size = min(measuredWidth, measuredHeight)
        setMeasuredDimension(size, size)
    }

    fun setConfiguration(mediaPlayer: MediaPlayer, url: String?){
        this.mediaPlayer = mediaPlayer
        preparePlayer(url)
    }

    private fun preparePlayer(url: String?) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            trackTimeLiveData.postValue(TIME_DEFAULT)
        }
        mediaPlayer.setOnCompletionListener {
            timerJob?.cancel()
            trackTimeLiveData.postValue(TIME_DEFAULT)
            isTrackPlaying = false
            invalidate()
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        trackTimeLiveData.postValue(getCurrentPlayerPosition())
        startTimer()
    }

    fun pausePlayer() {
        mediaPlayer.pause()
        timerJob?.cancel()
        trackTimeLiveData.postValue(getCurrentPlayerPosition())
    }

    private fun releasePlayer() {
        mediaPlayer.stop()
        mediaPlayer.release()
        trackTimeLiveData.value = TIME_DEFAULT
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = coroutineScope.launch {
            while (mediaPlayer.isPlaying) {
                delay(TIME_UPDATE_DELAY)
                trackTimeLiveData.postValue(getCurrentPlayerPosition())
            }
        }
    }

    private fun getCurrentPlayerPosition(): String {
        return dateFormat.format(mediaPlayer.currentPosition) ?: "00:00"
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
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        imageRect = RectF(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat())
    }

    override fun onDraw(canvas: Canvas) {
        val imageBitmap = if (isTrackPlaying) pauseImageBitmap else playImageBitmap
        imageBitmap?.let {
            canvas.drawBitmap(imageBitmap, null, imageRect, null)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                return true
            }
            MotionEvent.ACTION_UP -> {
                checkPlayer()
                return true
            }
        }

        return super.onTouchEvent(event)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        timerJob?.cancel()
        timerJob = null
        releasePlayer()
    }

    private fun checkPlayer(){
        if(isTrackPlaying){
            pausePlayer()
        } else {
            startPlayer()
        }
        isTrackPlaying = !isTrackPlaying
        invalidate()
    }

    companion object {
        private const val TIME_DEFAULT = "00:00"
        private const val TIME_UPDATE_DELAY = 300L
    }
}