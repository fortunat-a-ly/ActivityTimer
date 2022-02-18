package com.example.activitytimer.utils

import java.util.concurrent.TimeUnit
import kotlin.time.Duration.Companion.seconds

object DurationString {
    fun fromSeconds(duration: Long) = duration.seconds.toString()
    fun fromMilliseconds(duration: Long) = TimeUnit.MILLISECONDS.toSeconds(duration).seconds.toString()
}