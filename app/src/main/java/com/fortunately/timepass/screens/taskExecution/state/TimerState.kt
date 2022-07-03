package com.fortunately.timepass.screens.taskExecution.state

import com.fortunately.timepass.screens.taskExecution.CountDownTimerService

abstract class TimerState(protected val stateOwner: CountDownTimerService.LocalBinder) {

    abstract val canBePaused: Boolean

    abstract fun play()
    abstract fun skip()
    abstract fun finish()
}
