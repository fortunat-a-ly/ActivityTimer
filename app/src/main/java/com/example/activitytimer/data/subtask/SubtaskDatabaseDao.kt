package com.example.activitytimer.data.subtask

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.activitytimer.data.task.Task

@Dao
interface SubtaskDatabaseDao {
    @Insert
    fun insert(subtask: Subtask)

    @Query("SELECT * from subtask_table WHERE id = :key")
    fun get(key: Long): Subtask?

    @Query("SELECT * FROM subtask_table WHERE task_id = :taskKey ORDER BY name ASC")
    fun getTask(taskKey: Long): LiveData<List<Subtask>>

    @Query("DELETE FROM subtask_table")
    fun clear()
}