package com.example.activitytimer.taskExecution

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.activitytimer.data.subtask.Subtask
import com.example.activitytimer.data.subtask.SubtaskDatabaseDao
import com.example.activitytimer.utils.CountDownSecondsTimer
import kotlinx.coroutines.*

class TaskExecutionViewModel(
    private val database: SubtaskDatabaseDao,
    private val taskId: Long,
    application: Application
) : AndroidViewModel(application) {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main +  viewModelJob)

    private var subtasks: List<Subtask> = listOf()
    private var subtaskIndex = 0
    val currentSubtask: Subtask?
        get() = subtasks.getOrNull(subtaskIndex)

    private var _subtaskRepCount: Int = 0
    val subtaskRepCount: Int get() = _subtaskRepCount

    private val _allTasksDone: MutableLiveData<Boolean> = MutableLiveData(false)
    val allTasksDone: LiveData<Boolean> = _allTasksDone

    private val _taskChanged: MutableLiveData<Boolean> = MutableLiveData(false)
    val taskChanged: LiveData<Boolean> = _taskChanged

    private var timer: CountDownSecondsTimer? = null

    private val _durationInSeconds: MutableLiveData<Long> by lazy { MutableLiveData(currentSubtask?.time) }
    val durationInSeconds: LiveData<Long> = _durationInSeconds

    init {
        loadSubtasks()
    }

    private fun loadSubtasks(){
        uiScope.launch(Dispatchers.IO) {
            subtasks = database.getAllSubtasks(taskId)
        }
    }

    private fun launchTaskExecution() {
        startTaskWithBreakTimer()
    }

    private fun startTaskWithBreakTimer() {
        updateCurrentTaskTime(currentSubtask?.breakInterval ?: 0)
        timer = CountDownSecondsTimer(durationInSeconds.value ?: 0,
            { secs ->  Log.d("Life break", secs.toString());
            updateCurrentTaskTime(secs)},
            ::startTaskTimer)
        timer?.start()
    }

    private fun startTaskTimer() {
        updateCurrentTaskTime(currentSubtask?.time ?: 0)
        timer = CountDownSecondsTimer(
            durationInSeconds.value ?: 0,
            ::updateCurrentTaskTime,
            ::subtaskRepFinished)
        timer?.start()
    }

    private fun updateCurrentTaskTime(newTime: Long) {
        _durationInSeconds.value = newTime
    }

    private fun anyTasksLeft() : Boolean = subtaskIndex < subtasks.lastIndex

    private fun allSubtaskRepsDone(): Boolean = currentSubtask?.count == subtaskRepCount

    private fun automaticPlayback(): Boolean = currentSubtask?.playAutomatically == true

    private fun taskCompleted() {
        // SingleLiveEvent or better - Event wrapper
        _allTasksDone.value = true
        _allTasksDone.value = false
    }

    private fun taskChanged() {
        _taskChanged.value = true
        _taskChanged.value = false
    }

    private fun resetRepCount() {
        _subtaskRepCount = 0
    }

    private fun nextSubtask() {
        subtaskIndex += 1
    }

    private fun newSubtask() {
        if(anyTasksLeft()) {
            nextSubtask()
            resetRepCount()
            setSubtaskRepToInitialState()
        }
        else taskCompleted()
    }

    private fun setSubtaskRepToInitialState() {
        updateCurrentTaskTime(currentSubtask?.time ?: 0)
        taskChanged()
    }

    private fun subtaskRepDone() {
        _subtaskRepCount += 1
    }

    private fun subtaskRepFinished() {
        subtaskRepDone()
        if(allSubtaskRepsDone())
            newSubtask()
        else
            setSubtaskRepToInitialState()

        if(automaticPlayback())
            startTaskWithBreakTimer()
    }

    fun onStart() {
        launchTaskExecution()
    }

    fun onSkip() {
        timer?.cancel()
        newSubtask()
    }

    fun onPause(){
        timer?.cancel()
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}