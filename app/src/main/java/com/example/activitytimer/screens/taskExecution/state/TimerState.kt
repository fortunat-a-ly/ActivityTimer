package com.example.activitytimer.screens.taskExecution.state

import com.example.activitytimer.screens.taskExecution.CountDownTimerService

abstract class TimerState(protected val stateOwner: CountDownTimerService.LocalBinder) {

    abstract val canBePaused: Boolean

    abstract fun play()
    abstract fun skip()
    abstract fun finish()
}
