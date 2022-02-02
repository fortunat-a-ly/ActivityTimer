package com.example.activitytimer.createTask

import android.app.Application
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.example.activitytimer.createTask.viewModels.CreateTaskViewModel
import com.example.activitytimer.data.subtask.SubtaskDatabaseDao
import com.example.activitytimer.data.task.TaskDatabaseDao

class CreateTaskViewModelFactory(val database: TaskDatabaseDao,
                                 val databaseSubtask: SubtaskDatabaseDao,
                                 private val owner: SavedStateRegistryOwner, val application: Application)
    : AbstractSavedStateViewModelFactory(owner, null) {
    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        if (modelClass.isAssignableFrom(CreateTaskViewModel::class.java)) {
            return CreateTaskViewModel(database, databaseSubtask, handle, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}