package com.example.activitytimer.utils.timer

class CountDownSecondsTimerWithState(
    val secondsInFuture: Long,
    private val onTickExecute: (secondsUntilFinished: Long) -> Unit,
    private val onFinishExecute: () -> Unit) {

    private var INSTANCE: CountDownSecondsTimer = getInstance(0)

    private var _state: TimerState = TimerState.INITIAL
    val state: TimerState get() = _state

    private fun getInstance(countDownSeconds: Long): CountDownSecondsTimer = CountDownSecondsTimer(
        countDownSeconds, onTickExecute
    ) { _state = TimerState.FINISHED; onFinishExecute() }

    fun start(): CountDownSecondsTimerWithState {
        return start(secondsInFuture)
    }

    private fun start(secondsInFuture: Long): CountDownSecondsTimerWithState {
        _state = TimerState.RUNNING
        INSTANCE = getInstance(secondsInFuture)
        INSTANCE.start()
        return this
    }

    fun pause() {
        _state = TimerState.PAUSED
        INSTANCE.cancel()
    }

    fun cancel() {
        _state = TimerState.INITIAL
        INSTANCE.cancel()
    }

    fun resume(): CountDownSecondsTimerWithState {
        if(_state == TimerState.PAUSED) {
            start(INSTANCE.secondsLeft)
        }
        return this
    }

    companion object {
        enum class TimerState {
            INITIAL,
            RUNNING,
            PAUSED,
            FINISHED
        }
    }
}