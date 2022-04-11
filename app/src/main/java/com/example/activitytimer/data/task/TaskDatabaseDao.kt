package com.example.activitytimer.data.task

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.activitytimer.data.task.Task

@Dao
interface TaskDatabaseDao {
    @Insert
    fun insert(task: Task) : Long

    @Query("SELECT * from task_table WHERE id = :key")
    fun get(key: Long): Task?

    @Query("SELECT * FROM task_table ORDER BY name DESC")
    fun getAllTasks(): LiveData<List<Task>>

    @Query("DELETE FROM task_table")
    fun clear()
}