package com.fortunately.timepass.screens.taskExecution.state

import com.fortunately.timepass.screens.taskExecution.CountDownTimerService

class TimerPausedState(newStateOwner: CountDownTimerService.LocalBinder) : TimerState(newStateOwner) {

    override val canBePaused: Boolean = false

    override fun play() {
        stateOwner.timer?.resume()
        stateOwner._state.value = stateOwner.runningState
    }

    override fun skip() {
        stateOwner.timer?.cancel()
        stateOwner.newSubtask()
    }

    override fun finish() {
        stateOwner.saveTaskIntoHistory()
        stateOwner.finishTask()
        stateOwner._allTasksDone.value = true
    }
}
