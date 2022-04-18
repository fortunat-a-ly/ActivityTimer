package com.example.activitytimer.screens.taskExecution

import androidx.lifecycle.*
import com.example.activitytimer.data.subtask.Subtask
import com.example.activitytimer.data.subtask.SubtaskDatabaseDao
import com.example.activitytimer.utils.timer.CountDownSecondsTimerWithState
import com.example.activitytimer.utils.timer.CountDownSecondsTimerWithState.Companion.TimerState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class TaskExecutionViewModel @Inject constructor(
    private val database: SubtaskDatabaseDao,
    state: SavedStateHandle
) : ViewModel() {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main +  viewModelJob)

    private var subtasks: List<Subtask> = listOf()
    private var subtaskIndex = 0

    private val _currentSubtask: MutableLiveData<Subtask> = MutableLiveData(null)
    val currentSubtask: LiveData<Subtask> = _currentSubtask

    private var _subtaskRepCount: MutableLiveData<Int> = MutableLiveData(0)
    val subtaskRepCount: LiveData<Int> = _subtaskRepCount

    private val _allTasksDone: MutableLiveData<Boolean> = MutableLiveData(false)
    val allTasksDone: LiveData<Boolean> = _allTasksDone

    private var timer: CountDownSecondsTimerWithState? = null

    private var _durationInSeconds: MutableLiveData<Long> = MutableLiveData(0)
    val durationInSeconds: LiveData<Long> = _durationInSeconds

    private val _canBePaused: MutableLiveData<Boolean> = MutableLiveData(false)
    val canBePaused: LiveData<Boolean> = _canBePaused

    val taskId: Long = state.get<Long>("taskId")!!

    init {
        loadData()
    }

    private fun loadData(){
        uiScope.launch {
            loadUsers()
            setSubtask()
        }
    }

    private suspend fun loadUsers() {
        withContext(Dispatchers.IO) {
            subtasks = database.getAllSubtasks(taskId)
        }
    }

    private fun setSubtask() {
        _currentSubtask.value = subtasks[subtaskIndex]
        resetRepCount()
        setSubtaskRepToInitialState()
    }

    private fun initializeBreakTimer() {
        updateDuration(_currentSubtask.value?.breakInterval)
        timer = CountDownSecondsTimerWithState(durationInSeconds.value ?: 0,
                ::updateDuration,
                ::startSubtaskTimer)
    }

    private fun startSubtaskExecution() {
        startSubtaskWithBreakTimer()
    }

    private fun startSubtaskWithBreakTimer() {
        timer?.start()
    }

    private fun startSubtaskTimer() {
        updateDuration(_currentSubtask.value?.duration)
        timer = CountDownSecondsTimerWithState(
            durationInSeconds.value  ?: 0,
            ::updateDuration,
            ::subtaskRepFinished).start()
    }

    private fun resumeTaskExecution() {
        timer?.resume()
    }

    private fun updateDuration(newTime: Long?) {
        _durationInSeconds.value = newTime
    }

    private fun subtaskRepFinished() {
        subtaskRepDone()
        if(allSubtaskRepsDone())
            newSubtask()
        else
            setSubtaskRepToInitialState()

        if(automaticPlayback())
            startSubtaskExecution()
        else
            buttonToStart()
    }

    private fun anyTasksLeft(): Boolean = subtaskIndex < subtasks.lastIndex

    private fun allSubtaskRepsDone(): Boolean = _currentSubtask.value?.count == subtaskRepCount.value

    private fun automaticPlayback(): Boolean = _currentSubtask.value?.playAutomatically == true

    private fun newSubtask() {
        if(anyTasksLeft()) {
            nextSubtask()
            setSubtask()
        }
        else taskCompleted()
    }

    private fun setSubtaskRepToInitialState() {
        initializeBreakTimer()
    }

    private fun subtaskRepDone() {
        _subtaskRepCount.value = _subtaskRepCount.value?.plus(1)
    }

    private fun resetRepCount() {
        _subtaskRepCount.value = 0
    }

    private fun nextSubtask() {
        subtaskIndex += 1
    }

    fun onPlay() {
        when (timer?.state) {
            TimerState.RUNNING -> onPause()
            TimerState.PAUSED -> onResume()
            else -> onStart()
        }
    }

    private fun onStart() {
        buttonToPause()
        startSubtaskExecution()
    }

    private fun onResume() {
        buttonToPause()
        resumeTaskExecution()
    }

    private fun onPause() {
        buttonToStart()
        timer?.pause()
    }

    fun onSkip() {
        timer?.cancel()
        newSubtask()
        buttonToStart()
    }

    private fun buttonToPause() {
        _canBePaused.value = true
    }

    private fun buttonToStart() {
        _canBePaused.value = false
    }

    private fun taskCompleted() {
        // SingleLiveEvent or better - Event wrapper
        _allTasksDone.value = true
        _allTasksDone.value = false
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}