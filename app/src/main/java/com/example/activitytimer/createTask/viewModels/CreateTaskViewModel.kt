package com.example.activitytimer.createTask.viewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.example.activitytimer.data.subtask.Subtask
import com.example.activitytimer.data.subtask.SubtaskDatabaseDao
import com.example.activitytimer.data.task.Task
import com.example.activitytimer.data.task.TaskDatabaseDao
import kotlinx.coroutines.*

class CreateTaskViewModel(
    private val database: TaskDatabaseDao,
    private val databaseSubtask: SubtaskDatabaseDao,
    val state: SavedStateHandle,
    application: Application // ? error if add "val"
    )  : AndroidViewModel(application) {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main +  viewModelJob)

    val task: Task = Task()

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