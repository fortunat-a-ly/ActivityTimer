package com.fortunately.timepass.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation
import com.fortunately.timepass.data.doneTasks.DoneTask
import com.fortunately.timepass.data.task.Task

@Entity(tableName = "done_task_table")
data class DatedTask(
    @Embedded val datedRecord: DoneTask,
    @Relation(
        parentColumn = "task_id",
        entityColumn = "id"
    )
    val task: Task
)