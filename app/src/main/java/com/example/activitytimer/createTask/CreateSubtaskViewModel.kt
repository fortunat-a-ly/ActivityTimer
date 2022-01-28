package com.example.activitytimer.createTask

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.activitytimer.data.subtask.Subtask

class CreateSubtaskViewModel(private val state: SavedStateHandle) : ViewModel() {
    val subtask: Subtask = Subtask()

    private val _subtaskSaved: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    val subtaskSaved: LiveData<Boolean> = _subtaskSaved

    fun addSubtask() {
        state.set("subtask", subtask)
        _subtaskSaved.value = true
    }
}