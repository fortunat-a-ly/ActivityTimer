package com.example.activitytimer.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation
import com.example.activitytimer.data.doneTasks.DoneTask
import com.example.activitytimer.data.task.Task

@Entity(tableName = "done_task_table")
data class DatedTask(
    @Embedded val datedRecord: DoneTask,
    @Relation(
        parentColumn = "task_id",
        entityColumn = "id"
    )
    val task: Task
)