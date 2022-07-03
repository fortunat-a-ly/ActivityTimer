package com.fortunately.timepass.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fortunately.timepass.data.doneTasks.DoneTaskDatabaseDao
import com.fortunately.timepass.data.subtask.SubtaskDatabaseDao
import com.fortunately.timepass.data.task.Task
import com.fortunately.timepass.data.doneTasks.DoneTask
import com.fortunately.timepass.data.subtask.Subtask
import com.fortunately.timepass.data.task.TaskDatabaseDao

@Database(entities = [Task::class, Subtask::class, DoneTask::class], version = 3, exportSchema = false)
abstract class TaskDatabase : RoomDatabase() {

    abstract val taskDatabaseDao: TaskDatabaseDao
    abstract val subtaskDatabaseDao: SubtaskDatabaseDao
    abstract val doneTaskDao: DoneTaskDatabaseDao

    companion object {

        @Volatile
        private var INSTANCE: TaskDatabase? = null

        fun getInstance(context: Context): TaskDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        TaskDatabase::class.java,
                        "task_database"
                    )
                        .fallbackToDestructiveMigration() // wipe and rewrite data when migrating(eg. changing number of columns)
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}