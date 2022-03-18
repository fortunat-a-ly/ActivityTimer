package com.example.activitytimer.screens.listScreens

import com.example.activitytimer.data.ITask

class TaskListener (val clickListener: (taskId: Long) -> Unit) {
    fun onClick(task: ITask) = clickListener(task.id)
}