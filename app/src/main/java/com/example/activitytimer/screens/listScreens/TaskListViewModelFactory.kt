package com.example.activitytimer.screens.listScreens

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.activitytimer.data.DatabaseDao
import com.example.activitytimer.data.ITask
import com.example.activitytimer.data.subtask.Subtask
import com.example.activitytimer.data.subtask.SubtaskDatabaseDao
import com.example.activitytimer.data.task.Task
import com.example.activitytimer.data.task.TaskDatabaseDao

abstract class AbstractTaskListViewModelFactory<V : ITask>(val database: DatabaseDao<V>,
                                                           val taskId: Long,
                                                           val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskListViewModel::class.java)) {
            return TaskListViewModel(database, taskId, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}

class TaskListViewModelFactory(database: TaskDatabaseDao, application: Application) :
        AbstractTaskListViewModelFactory<Task>(database, 0, application)

class SubtaskListViewModelFactory(database: SubtaskDatabaseDao, taskId: Long, application: Application) :
    AbstractTaskListViewModelFactory<Subtask>(database, taskId, application)