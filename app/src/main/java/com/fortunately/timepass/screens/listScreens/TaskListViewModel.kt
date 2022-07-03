package com.fortunately.timepass.screens.listScreens

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.fortunately.timepass.data.task.Task
import com.fortunately.timepass.data.task.TaskDatabaseDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(database: TaskDatabaseDao) : ViewModel() {
    val tasks: LiveData<List<Task>> = database.get()
}