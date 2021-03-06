package com.example.activitytimer.createTask

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.example.activitytimer.data.subtask.Subtask
import com.example.activitytimer.data.task.Task
import com.example.activitytimer.data.task.TaskDatabaseDao
import kotlinx.coroutines.*

class CreateTaskViewModel(
    private val database: TaskDatabaseDao,
    val state: SavedStateHandle,
    application: Application // ? error if add "val"
    ) : AndroidViewModel(application) {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main +  viewModelJob)

    val task: Task = Task()
    //val subtask: Subtask? = Subtask(0, 0, "", 0, 0)
    val subtasks: MutableLiveData<Subtask> = MutableLiveData()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    init {
        //clearDatabase()

    }

    fun clearDatabase(){
        uiScope.launch {
            clear()
        }
    }

    suspend fun clear(){
        withContext(Dispatchers.IO){
            database.clear()
        }
    }

    fun saveToDatabase() {

        uiScope.launch {
            insert()
        }
    }

    private suspend fun insert() {
        withContext(Dispatchers.IO){
            database.insert(task)
        }
    }
}