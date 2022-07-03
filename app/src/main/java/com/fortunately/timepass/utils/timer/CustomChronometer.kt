package com.fortunately.timepass.utils.timer

import android.os.SystemClock
import android.widget.Chronometer

class CustomChronometer(private val chronometer: Chronometer) {
    var milliSeconds: Long = 0L

    val base: Long get() = SystemClock.elapsedRealtime() - milliSeconds

    val intervals = arrayListOf<Long>()

    fun start() {
        chronometer.start()
    }

    fun pause() {
        milliSeconds = SystemClock.elapsedRealtime() - chronometer.base
        chronometer.stop()
    }

    fun breakInterval() {
        val newMilliseconds = SystemClock.elapsedRealtime() - chronometer.base
        intervals.add(newMilliseconds - milliSeconds)
        milliSeconds = newMilliseconds
    }
}