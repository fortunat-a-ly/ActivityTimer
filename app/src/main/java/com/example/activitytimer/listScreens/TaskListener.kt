package com.example.activitytimer.listScreens

import com.example.activitytimer.data.ITask
import com.example.activitytimer.data.task.Task

class TaskListener (val clickListener: (taskId: Long) -> Unit) {
    fun onClick(task: ITask) = clickListener(task.id)
}