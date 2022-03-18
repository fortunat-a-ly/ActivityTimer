package com.example.activitytimer.screens.createTask.viewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.example.activitytimer.data.subtask.SubtaskDatabaseDao
import com.example.activitytimer.data.task.TaskDatabaseDao

class CreateTaskViewModelFactory(val database: TaskDatabaseDao,
                                 val databaseSubtask: SubtaskDatabaseDao,
                                 private val owner: SavedStateRegistryOwner, val application: Application)
    : AbstractSavedStateViewModelFactory(owner, null) {
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        Log.d("BugH", "before if")
        if (modelClass.isAssignableFrom(CreateTaskViewModel::class.java)) {
            Log.d("BugH", "if")
            return CreateTaskViewModel(database, databaseSubtask, handle, application) as T
        }
        Log.d("BugH", "exception")
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}