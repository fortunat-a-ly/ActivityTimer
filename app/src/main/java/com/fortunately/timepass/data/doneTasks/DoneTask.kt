package com.fortunately.timepass.data.doneTasks

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fortunately.timepass.data.ITask

@Entity(tableName = "done_task_table")
data class DoneTask(
    @ColumnInfo(name = "date")
    val date: Long,

    @ColumnInfo(name = "task_id")
    val taskId: Long
) : ITask {
    @PrimaryKey(autoGenerate = true)
    override var id: Long = 0
}