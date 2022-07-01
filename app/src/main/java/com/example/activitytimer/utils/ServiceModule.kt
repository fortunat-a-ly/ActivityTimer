package com.example.activitytimer.utils

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.activitytimer.MainActivity
import com.example.activitytimer.R
import com.example.activitytimer.screens.timer.TimerService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Qualifier

@Qualifier
@Retention
annotation class TaskTimerNotificationBuilder

@Qualifier
@Retention
annotation class TrackTimerNotificationBuilder

@Module
@InstallIn(ServiceComponent::class)
object ServiceModule {

    @ServiceScoped
    @Provides
    fun provideMainActivityPendingIntent(
        @ApplicationContext app: Context
    ) : PendingIntent {
        return PendingIntent.getActivity(
            app,
            0,
            Intent(app, MainActivity::class.java).also {
                it.action = Constants.ACTION_SHOW_TRACKING_FRAGMENT
            },
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    @ServiceScoped
    @Provides
    @TrackTimerNotificationBuilder
    fun provideTrackTimerNotificationBuilder(
        @ApplicationContext app: Context,
        pendingIntent: PendingIntent
    ) : NotificationCompat.Builder {
        return NotificationCompat.Builder(app, Constants.NOTIFICATION_TRACKING_CHANNEL_ID)
            .setAutoCancel(false)
            .setOngoing(true)
            .setContentTitle("Track Time")
            .setContentText("00:00:00")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentIntent(pendingIntent)
    }

    @ServiceScoped
    @Provides
    @TaskTimerNotificationBuilder
    fun provideTaskTimerNotificationBuilder(
        @ApplicationContext app: Context,
        pendingIntent: PendingIntent
    ) : NotificationCompat.Builder {
        return NotificationCompat.Builder(app, Constants.NOTIFICATION_TRACKING_CHANNEL_ID)
            .setAutoCancel(false)
            .setOngoing(true)
            .setContentTitle("Task")
            .setContentText("In progress")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentIntent(pendingIntent)
    }
}
