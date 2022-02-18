package com.example.activitytimer.data.subtask

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.activitytimer.data.DatabaseDao
import com.example.activitytimer.data.task.Task

@Dao
interface SubtaskDatabaseDao : DatabaseDao<Subtask> {
    @Insert
    fun insert(subtask: Subtask)

    @Insert
    fun insert(subtask: List<Subtask>)

    @Query("SELECT * from subtask_table")
    fun getAll(): List<Subtask>?

    @Query("SELECT * from subtask_table WHERE id = :key")
    fun get(key: Long): Subtask?
/*
    @Query("SELECT * FROM subtask_table WHERE task_id = :taskKey ORDER BY id ASC")
    fun getTask(taskKey: Long): List<Subtask>*/

    @Query("SELECT * FROM subtask_table WHERE task_id = :taskKey ORDER BY id ASC")
    override fun getAllTasks(taskKey: Long): LiveData<List<Subtask>>

    @Query("SELECT * FROM subtask_table WHERE task_id = :taskKey ORDER BY id ASC")
    fun getAllSubtasks(taskKey: Long): List<Subtask>

    @Query("DELETE FROM subtask_table")
    fun clear()
}