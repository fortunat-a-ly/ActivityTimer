package com.fortunately.timepass.screens.doneTasks

import androidx.lifecycle.ViewModel
import com.fortunately.timepass.data.task.TaskDatabaseDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DoneTasksViewModel @Inject constructor(tasksDao: TaskDatabaseDao) : ViewModel() {
    val list = tasksDao.getDatedTasks()
    val groupedByDate get() = list.value?.groupBy { it.task.name[0] } //TODO()
}