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
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

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
    TIMER, STOPWATCH, WORLD_TIME
}

/**
 * World time entry.
 */
data class WorldTime(
    val cityName: String,
    val time: String,
    val zoneId: String
)

/**
 * Represents the sound selected for the alarm.
 */
enum class AlarmSound {
    CLASSIC, RADAR, BEEP, GENTLE
}

/**
 * State object representing the timer's current progress and status.
 */
data class TimerState(
    val timeMillis: Long = 0L,
    val initialTimeMillis: Long = 0L,
    val status: TimerStatus = TimerStatus.INITIAL,
    val mode: TimerMode = TimerMode.TIMER,
    val worldTimes: List<WorldTime> = emptyList(),
    val laps: List<Long> = emptyList(),
    val selectedAlarmSound: AlarmSound = AlarmSound.CLASSIC,
    val isDarkMode: Boolean? = null,
    val language: AppLanguage = AppLanguage.SYSTEM
) {
    /**
     * Progress from 0.0 to 1.0. 
     */
    val progress: Float
        get() = if (initialTimeMillis > 0) {
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
    private var worldTimeJob: Job? = null
    private var mediaPlayer: MediaPlayer? = null

    private val availableCities = listOf(
        "London" to "Europe/London",
        "New York" to "America/New_York",
        "Tokyo" to "Asia/Tokyo",
        "Sydney" to "Australia/Sydney",
        "Paris" to "Europe/Paris",
        "Berlin" to "Europe/Berlin",
        "Dubai" to "Asia/Dubai",
        "Hong Kong" to "Asia/Hong_Kong",
        "Los Angeles" to "America/Los_Angeles",
        "Chicago" to "America/Chicago",
        "Singapore" to "Asia/Singapore",
        "Mumbai" to "Asia/Kolkata",
        "Seoul" to "Asia/Seoul",
        "Cairo" to "Africa/Cairo",
        "Sao Paulo" to "America/Sao_Paulo",
        "Moscow" to "Europe/Moscow",
        "Bangkok" to "Asia/Bangkok",
        "Istanbul" to "Europe/Istanbul",
        "Mexico City" to "America/Mexico_City",
        "Jakarta" to "Asia/Jakarta",
        "Lagos" to "Africa/Lagos",
        "Nairobi" to "Africa/Nairobi",
        "Johannesburg" to "Africa/Johannesburg",
        "Auckland" to "Pacific/Auckland",
        "Perth" to "Australia/Perth",
        "Honolulu" to "Pacific/Honolulu",
        "Anchorage" to "America/Anchorage",
        "Vancouver" to "America/Vancouver",
        "Toronto" to "America/Toronto",
        "Buenos Aires" to "America/Argentina/Buenos_Aires",
        "Santiago" to "America/Santiago",
        "Madrid" to "Europe/Madrid",
        "Rome" to "Europe/Rome",
        "Athens" to "Europe/Athens",
        "Stockholm" to "Europe/Stockholm",
        "Zurich" to "Europe/Zurich",
        "Riyadh" to "Asia/Riyadh",
        "Tehran" to "Asia/Tehran",
        "Manila" to "Asia/Manila",
        "Shanghai" to "Asia/Shanghai",
        "Lisbon" to "Europe/Lisbon",
        "Amsterdam" to "Europe/Amsterdam",
        "Brussels" to "Europe/Brussels",
        "Vienna" to "Europe/Vienna",
        "Warsaw" to "Europe/Warsaw",
        "Prague" to "Europe/Prague",
        "Budapest" to "Europe/Budapest",
        "Dublin" to "Europe/Dublin",
        "Oslo" to "Europe/Oslo",
        "Copenhagen" to "Europe/Copenhagen",
        "Helsinki" to "Europe/Helsinki",
        "Reykjavik" to "Atlantic/Reykjavik",
        "Tel Aviv" to "Asia/Tel_Aviv",
        "Amman" to "Asia/Amman",
        "Beirut" to "Asia/Beirut",
        "Baghdad" to "Asia/Baghdad",
        "Kuwait City" to "Asia/Kuwait",
        "Doha" to "Asia/Qatar",
        "Abu Dhabi" to "Asia/Dubai",
        "Muscat" to "Asia/Muscat",
        "Karachi" to "Asia/Karachi",
        "Dhaka" to "Asia/Dhaka",
        "Yangon" to "Asia/Yangon",
        "Ho Chi Minh City" to "Asia/Ho_Chi_Minh",
        "Kuala Lumpur" to "Asia/Kuala_Lumpur",
        "Taipei" to "Asia/Taipei",
        "Brisbane" to "Australia/Brisbane",
        "Adelaide" to "Australia/Adelaide",
        "Melbourne" to "Australia/Melbourne",
        "Fiji" to "Pacific/Fiji"
    )

    private val activeCities = MutableStateFlow(availableCities.take(5).map { it.first }.toSet())

    init {
        startWorldTimeUpdates()
    }

    private fun startWorldTimeUpdates() {
        worldTimeJob?.cancel()
        worldTimeJob = viewModelScope.launch {
            val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
            while (true) {
                val currentActive = activeCities.value
                val times = availableCities
                    .filter { it.first in currentActive }
                    .map { (name, zone) ->
                        val zonedDateTime = ZonedDateTime.now(ZoneId.of(zone))
                        WorldTime(name, zonedDateTime.format(formatter), zone)
                    }
                    .sortedBy { it.cityName }
                _state.update { it.copy(worldTimes = times) }
                delay(1000)
            }
        }
    }

    fun toggleCity(cityName: String) {
        activeCities.update { current ->
            if (current.contains(cityName)) {
                if (current.size > 1) current - cityName else current
            } else {
                if (current.size < 5) current + cityName else current
            }
        }
    }

    fun addLap() {
        if (_state.value.mode == TimerMode.STOPWATCH && _state.value.status == TimerStatus.RUNNING) {
            _state.update { it.copy(laps = listOf(it.timeMillis) + it.laps) }
        }
    }

    fun getAvailableCityNames() = availableCities.map { it.first }.sorted()

    /**
     * Configures the timer with a specific duration.
     */
    fun setTimer(durationMillis: Long) {
        _state.update {
            it.copy(
                timeMillis = durationMillis,
                initialTimeMillis = durationMillis,
                status = TimerStatus.INITIAL,
                mode = TimerMode.TIMER,
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
            val nextMode = when (it.mode) {
                TimerMode.TIMER -> TimerMode.STOPWATCH
                TimerMode.STOPWATCH -> TimerMode.WORLD_TIME
                TimerMode.WORLD_TIME -> TimerMode.TIMER
            }
            it.copy(
                mode = nextMode, 
                status = TimerStatus.INITIAL, 
                timeMillis = 0L, 
                initialTimeMillis = 0L,
                laps = emptyList()
            )
        }
    }

    /**
     * Starts or resumes the timer or stopwatch.
     */
    fun startTimer() {
        if (_state.value.status == TimerStatus.RUNNING) return
        if ((_state.value.mode == TimerMode.TIMER) && (_state.value.timeMillis <= 0)) return

        _state.update { it.copy(status = TimerStatus.RUNNING) }
        
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            var lastTime = System.currentTimeMillis()
            while (_state.value.status == TimerStatus.RUNNING) {
                delay(16)
                val currentTime = System.currentTimeMillis()
                val delta = currentTime - lastTime
                lastTime = currentTime
                
                _state.update {
                    if (it.mode == TimerMode.TIMER) {
                        val newRemainingTime = (it.timeMillis - delta).coerceAtLeast(0)
                        if (newRemainingTime <= 0) {
                            playAlarm()
                            it.copy(timeMillis = 0, status = TimerStatus.FINISHED)
                        } else {
                            it.copy(timeMillis = newRemainingTime)
                        }
                    } else if (it.mode == TimerMode.STOPWATCH) {
                        it.copy(timeMillis = it.timeMillis + delta)
                    } else {
                        it
                    }
                }
                // Break if timer finished
                if ((_state.value.mode == TimerMode.TIMER) && _state.value.timeMillis <= 0) break
            }
        }
    }

    fun setAlarmSound(sound: AlarmSound) {
        _state.update { it.copy(selectedAlarmSound = sound) }
    }

    fun toggleDarkMode(isDark: Boolean?) {
        _state.update { it.copy(isDarkMode = isDark) }
    }

    fun setLanguage(language: AppLanguage) {
        _state.update { it.copy(language = language) }
    }

    private fun playAlarm() {
        try {
            mediaPlayer?.release()
            val soundResId = when (_state.value.selectedAlarmSound) {
                AlarmSound.CLASSIC -> R.raw.after // Assuming this exists
                AlarmSound.RADAR -> R.raw.after // Placeholder for other sounds
                AlarmSound.BEEP -> R.raw.after
                AlarmSound.GENTLE -> R.raw.after
            }
            mediaPlayer = MediaPlayer.create(getApplication(), soundResId)
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
                    status = TimerStatus.INITIAL,
                    laps = emptyList()
                )
            } else {
                it.copy(
                    timeMillis = 0L,
                    status = TimerStatus.INITIAL,
                    laps = emptyList()
                )
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    override fun onCleared() {
        stopTimer()
        worldTimeJob?.cancel()
        worldTimeJob = null
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
