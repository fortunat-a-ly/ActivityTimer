package com.example.activitytimer.data.subtask

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

data class SubtaskListRepresentation (
    val id: Long,
    var name: String,
    var time: Long,
    var count: Int
)