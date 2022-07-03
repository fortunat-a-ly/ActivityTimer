package com.fortunately.timepass.screens.createTask.viewModels

import androidx.lifecycle.*
import com.fortunately.timepass.data.doneTasks.DoneTask
import com.fortunately.timepass.data.subtask.Subtask
import com.fortunately.timepass.data.subtask.SubtaskDatabaseDao
import com.fortunately.timepass.data.task.Task
import com.fortunately.timepass.data.task.TaskDatabaseDao
import com.fortunately.timepass.screens.timer.TimerService
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
            withContext(Dispatchers.IO) {
                if(subtasks.isNotEmpty())
                    task.duration = 0L
                val key = database.insert(task)

                if(!task.isConstant) {
                    database.insertDatedTask(DoneTask(date = Calendar.getInstance().timeInMillis, taskId = key))
                }

                relateSubtasksToTask(key)

                databaseSubtask.insert(subtasks)
            }
        }
    }

    private fun relateSubtasksToTask(taskKey: Long) {
        subtasks.forEach {
            it.taskId = taskKey
        }
    }
}