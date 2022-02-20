package com.example.activitytimer.utils.timer

import android.os.CountDownTimer
import java.util.concurrent.TimeUnit

class CountDownSecondsTimer(
    secondsInFuture: Long,
    val onTickExecute: (secondsUntilFinished: Long) -> Unit,
    val onFinishExecute: () -> Unit)
    : CountDownTimer(TimeUnit.SECONDS.toMillis(secondsInFuture), TimeUnit.SECONDS.toMillis(1L)) {

    private var millisLeft = TimeUnit.SECONDS.toMillis(secondsInFuture)
    val secondsLeft: Long get() = TimeUnit.MILLISECONDS.toSeconds(millisLeft)

    override fun onTick(millisUntilFinished: Long) {
        millisLeft = millisUntilFinished
        onTickExecute(TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished))
    }

    override fun onFinish() = onFinishExecute()
}