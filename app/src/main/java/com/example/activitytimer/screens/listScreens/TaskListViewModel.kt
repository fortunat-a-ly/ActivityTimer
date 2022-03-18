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

    val tasks: LiveData<List<T>> = database.getAllTasks(taskId)
}