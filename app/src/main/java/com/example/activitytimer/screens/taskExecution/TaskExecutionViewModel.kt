package com.example.activitytimer.screens.taskExecution

import android.app.Application
import android.content.Intent
import androidx.lifecycle.*
import com.example.activitytimer.data.doneTasks.DoneTask
import com.example.activitytimer.data.subtask.Subtask
import com.example.activitytimer.data.subtask.SubtaskDatabaseDao
import com.example.activitytimer.data.task.TaskDatabaseDao
import com.example.activitytimer.utils.timer.CountDownSecondsTimerWithState
import com.example.activitytimer.utils.timer.CountDownSecondsTimerWithState.Companion.TimerState
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject

@HiltViewModel
class TaskExecutionViewModel @Inject constructor(
    val state: SavedStateHandle
) : ViewModel() {

    val currentSubtask: LiveData<Subtask> = CountDownTimerService.currentSubtask
    val subtaskRepCount: LiveData<Int> = CountDownTimerService.subtaskRepCount
    val allTasksDone: LiveData<Boolean> = CountDownTimerService.allTasksDone
    val durationInSeconds: LiveData<Long> = CountDownTimerService.durationInSeconds
    val canBePaused: LiveData<Boolean> = CountDownTimerService.isTracking
}