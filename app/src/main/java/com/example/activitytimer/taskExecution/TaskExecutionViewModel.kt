package com.example.activitytimer.taskExecution

import android.app.Application
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

    private val _allTasksDone: MutableLiveData<Boolean> = MutableLiveData(false)
    val allTasksDone: LiveData<Boolean> = _allTasksDone

    private val _taskChanged: MutableLiveData<Boolean> = MutableLiveData(false)
    val taskChanged: LiveData<Boolean> = _taskChanged

    private var timer: CountDownSecondsTimer? = null

    private val _durationInSeconds: MutableLiveData<Long> by lazy { MutableLiveData(currentSubtask?.time) }
    val durationInSeconds: LiveData<Long> get() = _durationInSeconds

    init {
        loadSubtasks()
    }

    private fun loadSubtasks(){
        uiScope.launch(Dispatchers.IO) {
            subtasks = database.getAllSubtasks(taskId)
        }
    }

    private fun startTimer() {
        timer = CountDownSecondsTimer(
            durationInSeconds.value ?: 0,
            ::updateCurrentTaskTime,
            ::changeSubtask)
        timer?.start()
    }

    private fun updateCurrentTaskTime(newTime: Long) {
        _durationInSeconds.value = newTime
    }

    private fun anyTasksLeft() : Boolean = subtaskIndex < subtasks.lastIndex

    private fun taskCompleted() {
        // SingleLiveEvent or better - Event wrapper
        _allTasksDone.value = true
        _allTasksDone.value = false
    }

    private fun subtaskChanged() {
        _taskChanged.value = true
        _taskChanged.value = false
    }

    private fun nextTask() {
        subtaskIndex += 1
    }

    private fun changeSubtask() {
        if(anyTasksLeft()) {
            nextTask()
            updateCurrentTaskTime(currentSubtask?.time ?: 0)
            subtaskChanged()
        }
        else taskCompleted()
    }

    fun onStart() {
        startTimer()
    }

    fun onSkip() {
        timer?.cancel()
        changeSubtask()
    }

    fun onPause(){
        timer?.cancel()
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}