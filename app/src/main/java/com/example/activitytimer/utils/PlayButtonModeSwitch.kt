package com.example.activitytimer.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class PlayButtonModeSwitch {
    private val _canBePaused: MutableLiveData<Boolean> = MutableLiveData(false)
    val canBePaused: LiveData<Boolean> = _canBePaused

    private fun buttonToPause() {
        _canBePaused.value = true
    }

    private fun buttonToStart() {
        _canBePaused.value = false
    }
}