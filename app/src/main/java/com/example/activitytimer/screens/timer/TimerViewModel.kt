package com.example.activitytimer.screens.timer

import android.os.SystemClock
import android.widget.Chronometer
import androidx.lifecycle.ViewModel
import com.example.activitytimer.utils.timer.CustomChronometer

class TimerViewModel(private val layoutChronometer: Chronometer) : ViewModel() {
    var labelAllLater: Boolean = false

    var chronometer = CustomChronometer(layoutChronometer)

    private fun newBase() : Long = SystemClock.elapsedRealtime()
}