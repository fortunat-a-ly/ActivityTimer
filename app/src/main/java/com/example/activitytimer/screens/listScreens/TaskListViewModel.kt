package com.example.activitytimer.screens.listScreens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.activitytimer.data.DatabaseDao
import com.example.activitytimer.data.ITask

class TaskListViewModel <T : ITask> (
    private val database: DatabaseDao<T>,
    private val taskId: Long,
    application: Application
) : AndroidViewModel(application) {

    // private val viewModelJob = Job()
    // private val uiScope = CoroutineScope(Dispatchers.Main +  viewModelJob)
    val tasks: LiveData<List<T>> = database.getAllTasks(taskId)

/*    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }*/
}