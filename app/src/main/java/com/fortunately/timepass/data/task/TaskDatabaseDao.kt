package com.fortunately.timepass.data.task

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.fortunately.timepass.data.DatabaseDao
import com.fortunately.timepass.data.DatedTask
import com.fortunately.timepass.data.doneTasks.DoneTask

@Dao
interface TaskDatabaseDao : DatabaseDao<Task> {
    @Insert
    fun insert(task: Task) : Long

    @Insert
    fun insertDatedTask(task: DoneTask) : Long

    @Transaction
    @Query("SELECT * FROM done_task_table")
    fun getDatedTasks(): LiveData<List<DatedTask>>

    @Query("SELECT * from task_table WHERE id = :key")
    fun get(key: Long): Task?

    @Query("SELECT * from task_table WHERE id = :key")
    fun getLive(key: Long): LiveData<Task>

    @Query("SELECT * FROM task_table WHERE name != :taskKey ORDER BY name ASC")
    override fun getAllTasks(taskKey: Long): LiveData<List<Task>>

    @Query("SELECT *," +
            " CASE WHEN time = 0" +
            " THEN (SELECT SUM(time) FROM subtask_table WHERE subtask_table.task_id = task_table.id)" +
            " ELSE time END AS `time`" +
            " FROM task_table WHERE is_constant = 1 ORDER BY name ASC")
    fun get(): LiveData<List<Task>>

    @Query("SELECT * FROM done_task_table ORDER BY date DESC")
    fun getDoneTasks(): LiveData<List<DoneTask>>

    @Query("DELETE FROM task_table")
    fun clear()
}