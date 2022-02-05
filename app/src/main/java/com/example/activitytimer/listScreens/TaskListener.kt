package com.example.activitytimer.listScreens

import com.example.activitytimer.data.task.Task

class TaskListener (val clickListener: (task: Task) -> Unit) {
    fun onClick(task: Task) = clickListener(task)
}