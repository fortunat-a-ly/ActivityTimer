package com.example.activitytimer.taskList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.activitytimer.data.task.TaskDatabaseDao

class TaskListViewModel (
    private val database: TaskDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    val tasks = database.getAllTasks()
}