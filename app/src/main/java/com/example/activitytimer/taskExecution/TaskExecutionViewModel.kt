package com.example.activitytimer.taskExecution

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.activitytimer.data.task.Task
import com.example.activitytimer.data.task.TaskDatabaseDao
import kotlinx.coroutines.*

class TaskExecutionViewModel(
    private val database: TaskDatabaseDao,
    private val taskId: Long,
    application: Application
) : AndroidViewModel(application) {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main +  viewModelJob)

    var task: Task? = null
    val subtaskIndex = 0

    init {
        getCurrentTask()
    }

    private fun getCurrentTask() {
        uiScope.launch {
            withContext(Dispatchers.IO){
                task = database.get(taskId)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}