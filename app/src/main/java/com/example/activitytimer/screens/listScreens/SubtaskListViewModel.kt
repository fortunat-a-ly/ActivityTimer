package com.example.activitytimer.screens.listScreens

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.activitytimer.data.subtask.Subtask
import com.example.activitytimer.data.subtask.SubtaskDatabaseDao
import com.example.activitytimer.data.task.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class SubtaskListViewModel @Inject constructor(
    state: SavedStateHandle,
    database: SubtaskDatabaseDao
) : ViewModel() {
    val taskId: Long = state.get<Long>("taskId")!!
    val subtasks: LiveData<List<Subtask>> = database.getAllTasks(taskId)
}