package com.example.activitytimer.screens.listScreens

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.activitytimer.data.task.Task
import com.example.activitytimer.data.task.TaskDatabaseDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(database: TaskDatabaseDao) : ViewModel() {
    val tasks: LiveData<List<Task>> = database.get()
}