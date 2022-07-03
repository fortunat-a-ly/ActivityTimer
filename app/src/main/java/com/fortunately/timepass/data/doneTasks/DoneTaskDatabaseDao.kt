package com.fortunately.timepass.data.doneTasks

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface DoneTaskDatabaseDao {
    @Insert
    fun insert(task: DoneTask) : Long

/*    @Query("SELECT * FROM done_task_table ORDER BY date DESC")
    fun getAll(): LiveData<List<DoneTask>>*/

/*    @Query("DELETE FROM done_task_table")
    fun clear()*/
}