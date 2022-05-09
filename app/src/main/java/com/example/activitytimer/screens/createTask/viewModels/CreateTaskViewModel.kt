package com.example.activitytimer.screens.createTask.viewModels

import androidx.lifecycle.*
import com.example.activitytimer.data.doneTasks.DoneTask
import com.example.activitytimer.data.subtask.Subtask
import com.example.activitytimer.data.subtask.SubtaskDatabaseDao
import com.example.activitytimer.data.task.Task
import com.example.activitytimer.data.task.TaskDatabaseDao
import com.example.activitytimer.screens.timer.TimerService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class CreateTaskViewModel @Inject constructor(
    private val database: TaskDatabaseDao,
    private val databaseSubtask: SubtaskDatabaseDao,
    private val state: SavedStateHandle
    )  : ViewModel() {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main +  viewModelJob)

    val timeTracked: Boolean = state.get<Boolean>("isTracked") ?: false

    val task: Task = Task().apply {
        // if duration tracked then it`s one time activity
        isConstant = !timeTracked
        if(timeTracked)
            duration = TimerService.timeRunInMillis.value?.milliseconds!!.inWholeSeconds
    }

    val subtasks: List<Subtask> = if(timeTracked) TimerService.trackedSubtasks else CommonClass.subtasks

    val canBeSaved: Boolean get() = (subtasks.isNotEmpty() || task.duration > 0L) && task.name.trim().isNotEmpty()

    override fun onCleared() {
        super.onCleared()
        CommonClass.subtasks.clear()
        viewModelJob.cancel()
    }

    fun clearDatabase(){
        uiScope.launch {
            withContext(Dispatchers.IO){
                database.clear()
            }
        }
    }

    fun saveToDatabase() {
        uiScope.launch {
            withContext(Dispatchers.IO){
                sumTaskDuration()

                val key = database.insert(task)

                if(!task.isConstant) {
                    database.insertDatedTask(DoneTask(date = Calendar.getInstance().timeInMillis, taskId = key))
                }

                relateSubtasksToTask(key)

                databaseSubtask.insert(subtasks)
            }
        }
    }

    private fun sumTaskDuration() {
        subtasks.forEach {
            task.duration += it.duration
        }
    }

    private fun relateSubtasksToTask(taskKey: Long) {
        subtasks.forEach {
            it.taskId = taskKey
        }
    }
}