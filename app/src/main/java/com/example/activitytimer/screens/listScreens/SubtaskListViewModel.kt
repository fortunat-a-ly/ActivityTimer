package com.example.activitytimer.screens.listScreens

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.activitytimer.data.subtask.Subtask
import com.example.activitytimer.data.subtask.SubtaskDatabaseDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SubtaskListViewModel @Inject constructor(
    state: SavedStateHandle,
    database: SubtaskDatabaseDao
) : ViewModel() {
    val tasks: LiveData<List<Subtask>> = database.getAllTasks(state.get<Long>("taskId") ?: 0)
}