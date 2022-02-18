package com.example.activitytimer.utils

import android.util.Log
import java.util.concurrent.TimeUnit

object DurationString {
    fun fromSeconds(duration: Long) = from(duration, TimeUnit.SECONDS)
    fun fromMilliseconds(duration: Long) = from(duration, TimeUnit.MILLISECONDS)

    fun from(duration: Long, timeUnit: TimeUnit) : String {
        val hours = timeUnit.toHours(duration)
        val minutes = timeUnit.toMinutes(duration) -
                TimeUnit.HOURS.toMinutes(hours)
        val seconds = timeUnit.toSeconds(duration) -
                TimeUnit.MINUTES.toSeconds(timeUnit.toMinutes(duration))

        var formatted = ""
        if(hours > 0) formatted += "$hours:"
        formatted += "$minutes:$seconds"
        return formatted
    }
}