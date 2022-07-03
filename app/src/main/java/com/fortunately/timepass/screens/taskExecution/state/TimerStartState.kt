package com.fortunately.timepass.screens.taskExecution.state

import com.fortunately.timepass.screens.taskExecution.CountDownTimerService

class TimerStartState(newStateOwner: CountDownTimerService.LocalBinder) : TimerState(newStateOwner) {

    override val canBePaused: Boolean = false

    override fun play() {
        stateOwner.timer?.start()
        stateOwner.startTask()
        stateOwner._state.value = stateOwner.runningState
    }

    override fun skip() {
        stateOwner.timer?.pause()
        stateOwner.newSubtask()
    }

    override fun finish() { }
}
