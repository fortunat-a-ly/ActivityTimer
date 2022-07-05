package com.fortunately.timepass.utils

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.fortunately.timepass.R
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
            Intent(app, com.fortunately.timepass.MainActivity::class.java).also {
                it.action = Constants.ACTION_SHOW_TRACKING_FRAGMENT
            },
            PendingIntent.FLAG_IMMUTABLE
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
            .setSmallIcon(R.mipmap.ic_notification_foreground)
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
            .setSmallIcon(R.mipmap.ic_notification_foreground)
            .setContentIntent(pendingIntent)
    }
}
