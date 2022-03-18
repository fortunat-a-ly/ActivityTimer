package com.example.activitytimer.screens.timer

import android.widget.Chronometer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TimerViewModelFactory(private val chronometer: Chronometer) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TimerViewModel::class.java)) {
            return TimerViewModel(chronometer) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}