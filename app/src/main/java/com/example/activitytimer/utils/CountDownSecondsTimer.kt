package com.example.activitytimer.utils

import android.os.CountDownTimer
import java.util.concurrent.TimeUnit

class CountDownSecondsTimer(
    secondsInFuture: Long,
    val onTickExecute: (secondsUntilFinished: Long) -> Unit,
    val onFinishExecute: () -> Unit)
    : CountDownTimer(TimeUnit.SECONDS.toMillis(secondsInFuture), TimeUnit.SECONDS.toMillis(1L)) {

    override fun onTick(millisUntilFinished: Long) = onTickExecute(TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished))

    override fun onFinish() = onFinishExecute()
}