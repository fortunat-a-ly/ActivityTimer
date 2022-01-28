package com.example.activitytimer.data.subtask

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Subtask::class], version = 1, exportSchema = false)
abstract class SubtaskDatabase : RoomDatabase() {

    abstract val subtaskDatabaseDao: SubtaskDatabaseDao

    companion object {

        @Volatile
        private var INSTANCE: SubtaskDatabase? = null

        fun getInstance(context: Context): SubtaskDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SubtaskDatabase::class.java,
                        "subtask_database"
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