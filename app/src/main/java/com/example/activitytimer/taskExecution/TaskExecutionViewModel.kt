package com.example.activitytimer.taskExecution

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
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

    var subtasks: List<Subtask> = listOf()
    val subtaskIndex = 0


    init {
        getCurrentTask()

        while(subtaskIndex < subtasks.size) {
            object : CountDownTimer(subtasks[subtaskIndex].time, 1000) {

                override fun onTick(millisUntilFinished: Long) {
                    subtasks[subtaskIndex].time = millisUntilFinished / 1000
                }

                override fun onFinish() {
                    subtaskIndex.plus(1)
                }
            }.start()
        }
    }

    private fun getCurrentTask() {
        uiScope.launch {
            withContext(Dispatchers.IO){
                subtasks = database.getAllTasks(taskId).value ?: listOf()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}