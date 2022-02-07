package com.example.activitytimer.taskExecution

import android.app.Application
import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.activitytimer.data.subtask.Subtask
import com.example.activitytimer.data.subtask.SubtaskDatabaseDao
import kotlinx.coroutines.*

class TaskExecutionViewModel(
    private val database: SubtaskDatabaseDao,
    private val taskId: Long,
    application: Application
) : AndroidViewModel(application) {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main +  viewModelJob)

    val subtasks: LiveData<List<Subtask>> = database.getAllTasks(taskId)
    var subtaskIndex = 0
    private val _currentSubtask: MutableLiveData<Subtask> =
        MutableLiveData(
            if (subtasks.value?.isNotEmpty() == true)
                subtasks.value!![subtaskIndex]
            else Subtask(name = "aha", time = 20000L)
        )
    val currentSubtask: LiveData<Subtask> = _currentSubtask

    private val _allTasksDone: MutableLiveData<Boolean> = MutableLiveData(false) // SingleLiveEvent or better - Event wrapper
    val allTasksDone: LiveData<Boolean> = _allTasksDone

    private var timer: CountDownTimer? = null

    private fun getCurrentTask() {
        uiScope.launch {
            withContext(Dispatchers.IO){
            }
        }
    }

    fun startTimer() {
        if (subtasks.value?.isNotEmpty() == true) {
             timer = object : CountDownTimer(subtasks.value!![subtaskIndex].time, 1000) {

                override fun onTick(millisUntilFinished: Long) {
                    updateCurrentTaskTime(millisUntilFinished / 1000)
                    Log.d("Life", millisUntilFinished.toString())
                    Log.d("Life", subtaskIndex.toString())
                }

                override fun onFinish() {
                    nextTask()
                }
            }.start()
        }
    }

    fun updateCurrentTaskTime(newTime: Long){
        subtasks.value!![subtaskIndex].time = newTime
    }

    fun onSkip() {
        timer?.cancel()
        nextTask()
    }

    fun onPause(){
        timer?.cancel()
    }

    private fun anyTasksLeft() : Boolean = subtaskIndex < subtasks.value?.lastIndex ?: 0

    private fun taskCompleted() {
        _allTasksDone.value = true
        _allTasksDone.value = false
    }

    fun nextTask() {
        if(anyTasksLeft()) {
            subtaskIndex += 1
            _currentSubtask.value = subtasks.value?.get(subtaskIndex)
            Log.d("Life2", subtaskIndex.toString())
            Log.d("Life2", _currentSubtask.value.toString())
        }
        else taskCompleted()
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}