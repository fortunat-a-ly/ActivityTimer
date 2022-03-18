package com.example.activitytimer.data.doneTasks

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "done_task_table")
data class DoneTask(
    @PrimaryKey(autoGenerate = true)
    val id: Long,

    @ColumnInfo(name = "date")
    val date: Long,

    @ColumnInfo(name = "task_id")
    val taskId: Long
)