package com.example.activitytimer.screens.timer

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.example.activitytimer.R
import com.example.activitytimer.data.subtask.Subtask
import com.example.activitytimer.utils.Constants.ACTION_PAUSE_SERVICE
import com.example.activitytimer.utils.Constants.ACTION_START_OR_RESUME_SERVICE
import com.example.activitytimer.utils.Constants.ACTION_STOP_SERVICE
import com.example.activitytimer.utils.Constants.ACTION_TRACK_INTERVAL
import com.example.activitytimer.utils.Constants.NOTIFICATION_TIMER_ID
import com.example.activitytimer.utils.Constants.NOTIFICATION_TRACKING_CHANNEL_ID
import com.example.activitytimer.utils.Constants.NOTIFICATION_TRACKING_CHANNEL_NAME
import com.example.activitytimer.utils.Constants.TIMER_UPDATE_INTERVAL
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

@AndroidEntryPoint
class TimerService : LifecycleService() {

    var isFirstRun = true

    private val timeRunInSeconds = MutableLiveData<Long>()

    private var serviceKilled = false

    @Inject
    lateinit var baseNotificationBuilder: NotificationCompat.Builder

    lateinit var curNotificationBuilder: NotificationCompat.Builder

    private var timeIntervalBroke = 0L

    companion object {
        val timeRunInMillis = MutableLiveData<Long>(0L)
        val isTracking = MutableLiveData<Boolean>(false)

        val trackedSubtasks: MutableList<Subtask> = mutableListOf()

        var timeTrackedInMillis: Long = 0L

        val interval: Long get() = timeRunInMillis.value?.minus(timeTrackedInMillis)!!

        fun saveSubtask(subtask: Subtask) {
            val smth: Long = timeRunInMillis.value?.minus(timeTrackedInMillis)!!
            subtask.duration = smth.milliseconds.inWholeSeconds
            timeTrackedInMillis += smth

            trackedSubtasks.add(subtask)
        }
    }

    private fun postInitialValues() {
        isTracking.postValue(false)
        timeRunInSeconds.postValue(0L)
        timeRunInMillis.postValue(0L)
        isTracking.value = false
        timeRunInMillis.value = 0L
        timeRunInSeconds.value = 0L
        timeTrackedInMillis = 0L
        trackedSubtasks.clear()
    }

    override fun onCreate() {
        super.onCreate()

        Log.d("SER1", "onCreate")

        curNotificationBuilder = baseNotificationBuilder
        postInitialValues()

        isTracking.observe(this) {
            updateNotificationTrackingState(it)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("SER1", "onStart")
        intent?.let {
            when (it.action) {
                ACTION_PAUSE_SERVICE -> {
                    pauseService()
                }
                ACTION_STOP_SERVICE -> {
                    stopService()
                }
                ACTION_TRACK_INTERVAL -> {
                    trackInterval()
                }
                else -> {
                    if (isFirstRun) {
                        startForegroundService()
                        isFirstRun = false
                    } else {
                        if(!isTracking.value!!)
                            startTimer()
                    }
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private var isTimerEnabled = false
    private var lapTime = 0L
    private var timeRun = 0L
    private var timeStarted = 0L
    private var lastSecondTimestamp = 0L

    private fun startTimer() {
        isTracking.postValue(true)
        timeStarted = System.currentTimeMillis()
        isTimerEnabled = true
        CoroutineScope(Dispatchers.Main).launch {
            while (isTracking.value!!) {
                // time difference between now and timeStarted
                lapTime = System.currentTimeMillis() - timeStarted
                // post the new lapTime
                timeRunInMillis.postValue(timeRun + lapTime)
                if (timeRunInMillis.value!! >= lastSecondTimestamp + 1000L) {
                    timeRunInSeconds.postValue(timeRunInSeconds.value!! + 1)
                    lastSecondTimestamp += 1000L
                }
                delay(TIMER_UPDATE_INTERVAL)
            }
            timeRun += lapTime
        }
    }

    private fun pauseService() {
        isTracking.postValue(false)
        isTimerEnabled = false
    }

    private fun stopService() {
        serviceKilled = true
        isFirstRun = true
        pauseService()
        postInitialValues()
        stopForeground(true)
        stopSelf()
    }

    private fun trackInterval() {
        timeIntervalBroke = System.currentTimeMillis()
        if (timeRunInMillis.value != null){
            //Log.d("SER1", "not null")
            val interval = timeIntervalBroke - timeStarted - timeRun
            //Log.d("SER1", interval.toString())
            if(interval > 1000L) {
                pauseService()
            }
        }
    }

    private fun updateNotificationTrackingState(isTracking: Boolean) {
        val notificationActionText = if(isTracking) "Pause" else "Resume"
        val pendingIntent = if(isTracking) {
            val pauseIntent = Intent(this, TimerService::class.java).apply {
                action = ACTION_PAUSE_SERVICE
            }
            PendingIntent.getService(this, 1, pauseIntent, FLAG_UPDATE_CURRENT)
        } else {
            val resumeIntent = Intent(this, TimerService::class.java).apply {
                action = ACTION_START_OR_RESUME_SERVICE
            }
            PendingIntent.getService(this, 2, resumeIntent, FLAG_UPDATE_CURRENT)
        }

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        curNotificationBuilder.javaClass.getDeclaredField("mActions").apply {
            isAccessible = true
            set(curNotificationBuilder, ArrayList<NotificationCompat.Action>())
        }
        if(!serviceKilled) {
            curNotificationBuilder = baseNotificationBuilder
                .addAction(R.drawable.ic_launcher_background, notificationActionText, pendingIntent)
            notificationManager.notify(NOTIFICATION_TIMER_ID, curNotificationBuilder.build())
        }
    }

    private fun startForegroundService() {
        startTimer()
        isTracking.postValue(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        startForeground(NOTIFICATION_TIMER_ID, baseNotificationBuilder.build())

        timeRunInSeconds.observe(this) {
            if(!serviceKilled) {
                val notification = curNotificationBuilder
                    .setContentText(it.seconds.toString())
                notificationManager.notify(NOTIFICATION_TIMER_ID, notification.build())
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
            NOTIFICATION_TRACKING_CHANNEL_ID,
            NOTIFICATION_TRACKING_CHANNEL_NAME,
            IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }

}