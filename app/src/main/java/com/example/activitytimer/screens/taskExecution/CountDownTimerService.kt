package com.example.activitytimer.screens.taskExecution

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.activitytimer.R
import com.example.activitytimer.data.doneTasks.DoneTask
import com.example.activitytimer.data.subtask.Subtask
import com.example.activitytimer.data.subtask.SubtaskDatabaseDao
import com.example.activitytimer.data.task.TaskDatabaseDao
import com.example.activitytimer.utils.Constants
import com.example.activitytimer.utils.TaskTimerNotificationBuilder
import com.example.activitytimer.utils.timer.CountDownSecondsTimerWithState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@AndroidEntryPoint
class CountDownTimerService : LifecycleService() {

    //TODO("skipped all tasks")
    @Inject lateinit var subtasksDao: SubtaskDatabaseDao
    @Inject lateinit var taskDao: TaskDatabaseDao

    private var firstInitialisation = true

    private var serviceKilled = false

    private lateinit var notificationManager: NotificationManager

    @TaskTimerNotificationBuilder
    @Inject lateinit var baseNotificationBuilder: NotificationCompat.Builder

    private lateinit var curNotificationBuilder: NotificationCompat.Builder

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main +  viewModelJob)

    companion object {
        private var firstStart = true
        val taskExecuting: Boolean get() = !firstStart
        var taskId = -1L

        val isTracking = MutableLiveData<Boolean>(false)

        private var subtasks: MutableList<Subtask> = mutableListOf()
        private var subtaskIndex = 0

        private val _currentSubtask: MutableLiveData<Subtask> = MutableLiveData(null)
        val currentSubtask: LiveData<Subtask> = _currentSubtask

        private var _subtaskRepCount: MutableLiveData<Int> = MutableLiveData(0)
        val subtaskRepCount: LiveData<Int> = _subtaskRepCount

        private val _allTasksDone: MutableLiveData<Boolean> = MutableLiveData(false)
        val allTasksDone: LiveData<Boolean> = _allTasksDone

        var timer: CountDownSecondsTimerWithState? = null

        private var _durationInSeconds: MutableLiveData<Long> = MutableLiveData(0)
        val durationInSeconds: LiveData<Long> = _durationInSeconds
    }
    private fun onStart() {
        startSubtaskExecution()
    }

    private fun onSkip() {
        timer?.cancel()
        newSubtask()
    }

    private fun onPause() {
        timer?.pause()
    }

    private fun setSubtask() {
        _currentSubtask.value = subtasks[subtaskIndex]
        resetRepCount()
        setSubtaskRepToInitialState()
    }

    private fun initializeBreakTimer() {
        timer = if(currentSubtask.value!!.breakInterval > 0L) {
            updateDuration(_currentSubtask.value?.breakInterval)
            CountDownSecondsTimerWithState(durationInSeconds.value ?: 0,
                ::updateDuration,
                ::startSubtaskTimer)
        } else {
            updateDuration(_currentSubtask.value?.duration)
            CountDownSecondsTimerWithState(
                durationInSeconds.value  ?: 0,
                ::updateDuration,
                ::subtaskRepFinished)
        }
    }

    private fun startSubtaskExecution() {
        startSubtaskWithBreakTimer()
    }

    private fun startSubtaskWithBreakTimer() {
        timer?.start()
    }

    private fun startSubtaskTimer() {
        timer?.start()
    }

    private fun resumeTaskExecution() {
        timer?.resume()
    }

    private fun updateDuration(newTime: Long?) {
        _durationInSeconds.value = newTime
    }

    private fun subtaskRepFinished() {
        subtaskRepDone()
        if(allSubtaskRepsDone())
            newSubtask()
        else
            setSubtaskRepToInitialState()

        if(automaticPlayback())
            startSubtaskExecution()
        else
            isTracking.postValue(false)
    }

    private fun anyTasksLeft(): Boolean = subtaskIndex < subtasks.lastIndex

    private fun allSubtaskRepsDone(): Boolean = _currentSubtask.value?.count == subtaskRepCount.value

    private fun automaticPlayback(): Boolean = _currentSubtask.value?.playAutomatically == true



    private fun setSubtaskRepToInitialState() {
        initializeBreakTimer()
    }

    private fun subtaskRepDone() {
        _subtaskRepCount.value = _subtaskRepCount.value?.plus(1)
    }

    private fun resetRepCount() {
        _subtaskRepCount.value = 0
    }

    private fun nextSubtask() {
        subtaskIndex += 1
    }

    private fun newSubtask() {
        if(anyTasksLeft()) {
            nextSubtask()
            setSubtask()
        }
        else taskCompleted()
    }

    private fun taskCompleted() {
        saveDoneTask()

        // SingleLiveEvent or better - Event wrapper
        _allTasksDone.value = true

        val notification = baseNotificationBuilder
            .clearActions()
            .setContentText("Well done!")
            .setOngoing(false)
        notificationManager.notify(Constants.NOTIFICATION_TASK_TIMER_ID, notification.build())

        //remove service from foreground state and make notification dismissible
        stopForeground(false)
    }


    private fun postInitialValues() {
        taskId = -1
        firstStart = true
        firstInitialisation = true
        isTracking.postValue(false)
        subtasks.clear()
        subtaskIndex = 0
        _currentSubtask.postValue(null)
        _subtaskRepCount.postValue(0)
        _allTasksDone.postValue(false)
        timer = null
        _durationInSeconds.postValue(0)
    }

    private fun saveDoneTask() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                if(taskId > -1L)
                    taskDao.insertDatedTask(DoneTask(date = Calendar.getInstance().timeInMillis, taskId = taskId))
            }
        }
    }

    override fun onCreate() {
        super.onCreate()

        curNotificationBuilder = baseNotificationBuilder
        postInitialValues()
    }

    private fun loadData(){
        uiScope.launch {
            loadUsers()
            setSubtask()
        }
    }

    private suspend fun loadUsers() {
        withContext(Dispatchers.IO) {
            subtasks = subtasksDao.getAllSubtasks(taskId) as MutableList<Subtask>
            if(subtasks.isEmpty())
                subtasks.add(  Subtask(taskDao.get(taskId)!!))
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                Constants.ACTION_INITIALIZE_CURRENT_SUBTASK -> {
                    if(firstInitialisation) {
                        taskId = it.getLongExtra("taskId", -1L)
                        loadData()
                        firstInitialisation = false
                    }
                }
                Constants.ACTION_PAUSE_SERVICE -> {
                    pauseService()
                }
                Constants.ACTION_STOP_SERVICE -> {
                    stopService()
                }
                Constants.ACTION_STOP_SERVICE_BEFOREHAND -> {
                    if(taskExecuting) {
                        taskCompleted()
                        stopService()
                    }
                }
                Constants.ACTION_TASK_TIMER_SKIP -> {
                    onSkip()
                }
                else -> {
                    if (firstStart) {
                        startForegroundService()
                        firstStart = false
                    } else {
                        if(!isTracking.value!!)
                            onResume()
                    }
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private var isTimerEnabled = false

    private fun onResume() {
        isTracking.postValue(true)
        isTimerEnabled = true
        resumeTaskExecution()
        updateNotificationTrackingState(true)
    }

    private fun startTimer() {
        isTracking.postValue(true)
        isTimerEnabled = true
        onStart()
        updateNotificationTrackingState(true)
    }

    private fun pauseService() {
        isTracking.postValue(false)
        isTimerEnabled = false
        onPause()
        if(!firstStart)
            updateNotificationTrackingState(false)
    }

    private fun stopService() {
        serviceKilled = true
        firstStart = true
        pauseService()
        postInitialValues()
        stopForeground(true)
        stopSelf()
    }

    private fun updateNotificationTrackingState(isTracking: Boolean) {
        val notificationActionText = if(isTracking) "Pause" else "Resume"
        val pendingIntent = if(isTracking) {
            val pauseIntent = Intent(this, CountDownTimerService::class.java).apply {
                action = Constants.ACTION_PAUSE_SERVICE
            }
            PendingIntent.getService(this, 1, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        } else {
            val resumeIntent = Intent(this, CountDownTimerService::class.java).apply {
                action = Constants.ACTION_START_OR_RESUME_SERVICE
            }
            PendingIntent.getService(this, 2, resumeIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        curNotificationBuilder.javaClass.getDeclaredField("mActions").apply {
            isAccessible = true
            set(curNotificationBuilder, ArrayList<NotificationCompat.Action>())
        }
        if(!serviceKilled) {
            curNotificationBuilder = baseNotificationBuilder
                .addAction(R.drawable.ic_launcher_background, notificationActionText, pendingIntent)
            notificationManager.notify(Constants.NOTIFICATION_TASK_TIMER_ID, curNotificationBuilder.build())
        }
    }

    private fun startForegroundService() {
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }

        startForeground(Constants.NOTIFICATION_TASK_TIMER_ID, baseNotificationBuilder.build())

        _durationInSeconds.observe(this) {
            if(!serviceKilled) {
                val notification = curNotificationBuilder
                    .setContentText(it.seconds.toString())
                notificationManager.notify(Constants.NOTIFICATION_TASK_TIMER_ID, notification.build())
            }
        }

        startTimer()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            Constants.NOTIFICATION_TRACKING_CHANNEL_ID,
            Constants.NOTIFICATION_TRACKING_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }

}