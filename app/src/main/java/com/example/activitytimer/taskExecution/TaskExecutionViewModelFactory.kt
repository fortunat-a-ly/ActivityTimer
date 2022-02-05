package com.example.activitytimer.taskExecution

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.activitytimer.data.subtask.SubtaskDatabaseDao
import com.example.activitytimer.data.task.TaskDatabaseDao

class TaskExecutionViewModelFactory(val database: SubtaskDatabaseDao,
                                    val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskExecutionViewModel::class.java)) {
            return TaskExecutionViewModelFactory(database, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}