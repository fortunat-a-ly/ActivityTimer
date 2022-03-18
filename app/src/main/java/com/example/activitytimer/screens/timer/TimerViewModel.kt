package com.example.activitytimer.screens.timer

import android.os.SystemClock
import android.widget.Chronometer
import androidx.lifecycle.ViewModel
import com.example.activitytimer.utils.timer.CustomChronometer

class TimerViewModel() : ViewModel() {
    var labelAllLater: Boolean = false

    var milliSeconds: Long = 0L

    var subtaskMlSeconds: Long = 0L

    var base: Long = SystemClock.elapsedRealtime()

    val newBase: Long get() = SystemClock.elapsedRealtime() - milliSeconds

    val intervals = arrayListOf<Long>()

    var paused = true
}