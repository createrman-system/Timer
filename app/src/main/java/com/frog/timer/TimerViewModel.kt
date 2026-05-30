package com.frog.timer

import android.app.Application
import android.media.MediaPlayer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Represents the current status of the timer/stopwatch.
 */
enum class TimerStatus {
    INITIAL, RUNNING, PAUSED, FINISHED
}

/**
 * Represents the mode of the timer.
 */
enum class TimerMode {
    TIMER, STOPWATCH
}

/**
 * State object representing the timer's current progress and status.
 */
data class TimerState(
    val timeMillis: Long = 0L,
    val initialTimeMillis: Long = 0L,
    val status: TimerStatus = TimerStatus.INITIAL,
    val mode: TimerMode = TimerMode.TIMER
) {
    /**
     * Progress from 0.0 to 1.0 for TIMER mode. 
     * For STOPWATCH, it could represent something else or be 0.
     */
    val progress: Float
        get() = if (mode == TimerMode.TIMER && initialTimeMillis > 0) {
            timeMillis.toFloat() / initialTimeMillis.toFloat()
        } else {
            0f
        }
}

/**
 * ViewModel responsible for managing the countdown timer and stopwatch logic.
 */
class TimerViewModel(application: Application) : AndroidViewModel(application) {

    private val _state = MutableStateFlow(TimerState())
    val state: StateFlow<TimerState> = _state.asStateFlow()

    private var timerJob: Job? = null
    private var mediaPlayer: MediaPlayer? = null

    /**
     * Configures the timer with a specific duration.
     */
    fun setTimer(durationMillis: Long) {
        _state.update {
            it.copy(
                timeMillis = durationMillis,
                initialTimeMillis = durationMillis,
                status = TimerStatus.INITIAL,
                mode = TimerMode.TIMER
            )
        }
        stopTimer()
    }

    /**
     * Switches between Timer and Stopwatch modes.
     */
    fun toggleMode() {
        stopTimer()
        _state.update {
            val nextMode = if (it.mode == TimerMode.TIMER) TimerMode.STOPWATCH else TimerMode.TIMER
            TimerState(mode = nextMode)
        }
    }

    /**
     * Starts or resumes the timer or stopwatch.
     */
    fun startTimer() {
        if (_state.value.status == TimerStatus.RUNNING) return
        if (_state.value.mode == TimerMode.TIMER && _state.value.timeMillis <= 0) return

        _state.update { it.copy(status = TimerStatus.RUNNING) }
        
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_state.value.status == TimerStatus.RUNNING) {
                delay(16)
                _state.update {
                    if (it.mode == TimerMode.TIMER) {
                        val newRemainingTime = (it.timeMillis - 16).coerceAtLeast(0)
                        if (newRemainingTime <= 0) {
                            playAlarm()
                            it.copy(timeMillis = 0, status = TimerStatus.FINISHED)
                        } else {
                            it.copy(timeMillis = newRemainingTime)
                        }
                    } else {
                        // Stopwatch mode: increment time
                        it.copy(timeMillis = it.timeMillis + 16)
                    }
                }
                // Break if timer finished
                if (_state.value.mode == TimerMode.TIMER && _state.value.timeMillis <= 0) break
            }
        }
    }

    private fun playAlarm() {
        try {
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer.create(getApplication(), R.raw.after)
            mediaPlayer?.start()
            mediaPlayer?.setOnCompletionListener {
                it.release()
                mediaPlayer = null
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Pauses the timer/stopwatch.
     */
    fun pauseTimer() {
        if (_state.value.status != TimerStatus.RUNNING) return
        
        timerJob?.cancel()
        _state.update { it.copy(status = TimerStatus.PAUSED) }
    }

    /**
     * Resets the timer/stopwatch to its initial state.
     */
    fun resetTimer() {
        stopTimer()
        _state.update {
            if (it.mode == TimerMode.TIMER) {
                it.copy(
                    timeMillis = it.initialTimeMillis,
                    status = TimerStatus.INITIAL
                )
            } else {
                it.copy(
                    timeMillis = 0L,
                    status = TimerStatus.INITIAL
                )
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    override fun onCleared() {
        super.onCleared()
        stopTimer()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
