package com.example.activitytimer.utils

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.activitytimer.data.subtask.Subtask
import com.example.activitytimer.data.task.Task

@BindingAdapter("timeText")
fun TextView.setTime(item: Task) {
    val minutes = item.time / 60
    val seconds = item.time % 60
    text = (minutes.toString() + ":" + seconds.toString())
}
/*

@BindingAdapter("timeText")
fun TextView.setTime(item: Subtask) {
    val minutes = item.time / 60
    val seconds = item.time % 60
    text = (minutes.toString() + ":" + seconds.toString())
}
*/
