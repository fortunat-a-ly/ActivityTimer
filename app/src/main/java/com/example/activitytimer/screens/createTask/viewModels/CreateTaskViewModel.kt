package com.example.activitytimer.screens.createTask.viewModels

import androidx.lifecycle.*
import com.example.activitytimer.data.subtask.Subtask
import com.example.activitytimer.data.subtask.SubtaskDatabaseDao
import com.example.activitytimer.data.task.Task
import com.example.activitytimer.data.task.TaskDatabaseDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class CreateTaskViewModel @Inject constructor(
    private val database: TaskDatabaseDao,
    private val databaseSubtask: SubtaskDatabaseDao,
    private val state: SavedStateHandle
    )  : ViewModel() {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main +  viewModelJob)

    val task: Task = Task().apply {
        // if duration tracked then it`s one time activity
        isConstant = state.get<Long>("duration") == 1L
    }

    val timeTracked = state.get<Long>("duration") != 1L

    val subtasks: List<Subtask> = CommonClass.subtasks

    override fun onCleared() {
        super.onCleared()
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
                val key = database.insert(task)
                // Log.d("Life", subtasks.toString())
                subtasks.forEach { it ->
                    it.taskId = key
                }
                // Log.d("Life", "finish")
                databaseSubtask.insert(subtasks)
                CommonClass.subtasks.clear()
            }
        }
    }
}