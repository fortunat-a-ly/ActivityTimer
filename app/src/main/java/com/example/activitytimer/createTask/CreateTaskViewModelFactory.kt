package com.example.activitytimer.createTask

import android.app.Activity
import android.app.Application
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.savedstate.SavedStateRegistryOwner
import com.example.activitytimer.data.task.TaskDatabaseDao

class CreateTaskViewModelFactory(val database: TaskDatabaseDao,
                                 private val owner: SavedStateRegistryOwner, val application: Application)
    : AbstractSavedStateViewModelFactory(owner, null) {
    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        if (modelClass.isAssignableFrom(CreateTaskViewModel::class.java)) {
            return CreateTaskViewModel(database, handle, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}