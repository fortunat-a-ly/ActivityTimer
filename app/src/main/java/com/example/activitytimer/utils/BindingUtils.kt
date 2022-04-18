package com.example.activitytimer.utils

import android.widget.Button
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.activitytimer.R
import com.example.activitytimer.data.task.Task

@BindingAdapter("timeText")
fun TextView.setTime(item: Task) {
    val minutes = item.duration / 60
    val seconds = item.duration % 60
    text = (minutes.toString() + ":" + seconds.toString())
}

@BindingAdapter("text")
fun Button.setText(canBePaused: Boolean) {
    text = if (canBePaused)
        resources.getString(R.string.pause)
    else resources.getString(R.string.start)
}
/*

@BindingAdapter("timeText")
fun TextView.setTime(item: Subtask) {
    val minutes = item.time / 60
    val seconds = item.time % 60
    text = (minutes.toString() + ":" + seconds.toString())
}
*/
