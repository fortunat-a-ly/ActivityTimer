package com.example.activitytimer.taskList

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.activitytimer.data.task.TaskDatabaseDao

class TaskListViewModelFactory(val database: TaskDatabaseDao,
                               val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskListViewModel::class.java)) {
            return TaskListViewModel(database, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}