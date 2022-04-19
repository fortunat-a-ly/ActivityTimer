package com.example.activitytimer.screens.doneTasks

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.activitytimer.data.doneTasks.DoneTask
import com.example.activitytimer.data.task.TaskDatabaseDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DoneTasksViewModel @Inject constructor(tasksDao: TaskDatabaseDao) : ViewModel() {
    val list: LiveData<List<DoneTask>> = tasksDao.getDoneTasks()
}