package com.fortunately.timepass.utils

import android.content.Context
import com.fortunately.timepass.data.TaskDatabase
import com.fortunately.timepass.data.subtask.SubtaskDatabaseDao
import com.fortunately.timepass.data.task.TaskDatabaseDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Provides
    fun provideTaskDao(appDatabase: TaskDatabase): TaskDatabaseDao {
        return appDatabase.taskDatabaseDao
    }

    @Provides
    fun provideSubtaskDao(appDatabase: TaskDatabase): SubtaskDatabaseDao {
        return appDatabase.subtaskDatabaseDao
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): TaskDatabase {
        return TaskDatabase.getInstance(appContext)
    }
}