package com.example.activitytimer.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query

@Dao
interface DatabaseDao<T : ITask> {
    @Query("")
    fun getAllTasks(taskKey: Long) : LiveData<List<T>>
}