package com.fortunately.timepass.screens.listScreens

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.fortunately.timepass.data.subtask.Subtask
import com.fortunately.timepass.data.subtask.SubtaskDatabaseDao
import com.fortunately.timepass.data.task.TaskDatabaseDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SubtaskListViewModel @Inject constructor(
    state: SavedStateHandle,
    subtasksDao: SubtaskDatabaseDao,
    taskDao: TaskDatabaseDao
) : ViewModel() {
    val taskId: Long = state.get<Long>("taskId")!!
    val subtasks: LiveData<List<Subtask>> = subtasksDao.getAllTasks(taskId)

    val task = taskDao.getLive(taskId)
}