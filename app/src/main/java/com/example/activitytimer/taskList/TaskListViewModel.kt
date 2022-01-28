package com.example.activitytimer.taskList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.activitytimer.data.task.TaskDatabaseDao

class TaskListViewModel (
    private val database: TaskDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    // private val viewModelJob = Job()
    // private val uiScope = CoroutineScope(Dispatchers.Main +  viewModelJob)

    val tasks = database.getAllTasks()

/*    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }*/
}