package com.example.activitytimer.screens.createTask.viewModels

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.activitytimer.data.subtask.Subtask
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@ExperimentalTime
class CreateSubtaskViewModel(private val state: SavedStateHandle) : ViewModel() {
    val subtask: Subtask = Subtask()

    private val _subtaskSaved: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    val subtaskSaved: LiveData<Boolean> = _subtaskSaved

    fun addSubtask() {
        CommonClass.subtasks.add(subtask)
        _subtaskSaved.value = true
    }

/*    @Bindable
    fun getDuration(): String {
        return Duration.Companion.seconds(subtask.time).toString()
    }*/
}