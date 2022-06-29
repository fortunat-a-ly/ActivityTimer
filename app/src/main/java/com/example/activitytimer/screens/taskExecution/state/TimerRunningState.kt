package com.example.activitytimer.screens.taskExecution.state

import com.example.activitytimer.screens.taskExecution.CountDownTimerService

class TimerRunningState(stateOwner: CountDownTimerService.LocalBinder) : TimerState(stateOwner) {

    override val canBePaused: Boolean = true

    override fun play() {
        stateOwner.timer?.pause()
        stateOwner._state.value= stateOwner.pausedState
    }

    override fun skip() {
        stateOwner.timer?.pause()
        stateOwner.newSubtask()
        stateOwner._state.value = stateOwner.pausedState
    }

    override fun finish() {
        stateOwner.timer?.pause()
        stateOwner._state.value = stateOwner.pausedState
        stateOwner.finishTask()
        stateOwner._allTasksDone.value = true
    }
}
