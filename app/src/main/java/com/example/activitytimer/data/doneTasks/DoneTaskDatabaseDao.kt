package com.example.activitytimer.data.doneTasks

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DoneTaskDatabaseDao {
    @Insert
    fun insert(task: DoneTask) : Long

/*    @Query("SELECT * FROM done_task_table ORDER BY date DESC")
    fun getAll(): LiveData<List<DoneTask>>*/

/*    @Query("DELETE FROM done_task_table")
    fun clear()*/
}