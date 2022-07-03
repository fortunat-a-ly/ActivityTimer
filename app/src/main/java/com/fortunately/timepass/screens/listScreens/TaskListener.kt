package com.fortunately.timepass.screens.listScreens

import com.fortunately.timepass.data.ITask

class TaskListener (val clickListener: (taskId: Long) -> Unit) {
    fun onClick(task: ITask) = clickListener(task.id)
}