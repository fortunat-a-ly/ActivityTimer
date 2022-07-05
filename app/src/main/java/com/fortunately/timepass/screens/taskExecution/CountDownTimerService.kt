package com.fortunately.timepass.screens.taskExecution

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.fortunately.timepass.data.doneTasks.DoneTask
import com.fortunately.timepass.data.subtask.Subtask
import com.fortunately.timepass.data.subtask.SubtaskDatabaseDao
import com.fortunately.timepass.data.task.TaskDatabaseDao
import com.fortunately.timepass.screens.taskExecution.state.TimerPausedState
import com.fortunately.timepass.screens.taskExecution.state.TimerRunningState
import com.fortunately.timepass.screens.taskExecution.state.TimerStartState
import com.fortunately.timepass.screens.taskExecution.state.TimerState
import com.fortunately.timepass.utils.Constants
import com.fortunately.timepass.utils.TaskTimerNotificationBuilder
import com.fortunately.timepass.utils.timer.CountDownSecondsTimerWithState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject
import kotlin.properties.Delegates

@AndroidEntryPoint
class CountDownTimerService : LifecycleService() {

    private val binder: LocalBinder = LocalBinder()

    //TODO("skipped all tasks")
    @Inject lateinit var subtasksDao: SubtaskDatabaseDao
    @Inject lateinit var taskDao: TaskDatabaseDao

    @TaskTimerNotificationBuilder
    @Inject lateinit var baseNotificationBuilder: NotificationCompat.Builder

    private lateinit var notificationManager: NotificationManager
    //lateinit var taskSession: TaskSession

    private var taskId by Delegates.notNull<Long>()

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }

        if (binder.state.value == binder.startState) {
            taskId = intent.getLongExtra("taskId", -1L)
            binder.loadData()
        }

/*        taskSession = TaskSession(subtasksDao, taskDao, binder,
            notificationManager, taskId)*/
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        if(binder.state.value == binder.startState)
            binder.finishTask()
        return super.onUnbind(intent)
    }

    fun play() {
        binder._state.value?.play()
    }

    fun skip() {
        binder._state.value?.skip()
    }

    fun finish() {
        binder._state.value?.finish()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            Constants.NOTIFICATION_TRACKING_CHANNEL_ID,
            Constants.NOTIFICATION_TRACKING_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        )
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    inner class LocalBinder : Binder() {
        // Return this instance of LocalService so clients can call public methods
        fun getService(): CountDownTimerService = this@CountDownTimerService

        val startState: TimerState = TimerStartState(this)
        val runningState: TimerState = TimerRunningState(this)
        val pausedState: TimerState = TimerPausedState(this)
        internal val _state: MutableLiveData<TimerState> = MutableLiveData(startState)
        val state: LiveData<TimerState> get() = _state

        private val viewModelJob = Job()
        private val uiScope = CoroutineScope(Dispatchers.Main +  viewModelJob)

        private var subtasks: MutableList<Subtask> = mutableListOf()
        var subtaskIndex = 0

        private val _currentSubtask: MutableLiveData<Subtask> = MutableLiveData(null)
        val currentSubtask: LiveData<Subtask> = _currentSubtask

        private val _subtaskRepCount: MutableLiveData<Int> = MutableLiveData(0)
        val subtaskRepCount: LiveData<Int> = _subtaskRepCount

        val _allTasksDone: MutableLiveData<Boolean> = MutableLiveData(false)
        val allTasksDone: LiveData<Boolean> = _allTasksDone

        private val _skippedAllTasks: MutableLiveData<Boolean> = MutableLiveData(false)
        val skippedAllTasks: LiveData<Boolean> = _skippedAllTasks

        var timer: CountDownSecondsTimerWithState? = null

        private val _durationInSeconds: MutableLiveData<Long> = MutableLiveData(0)
        val durationInSeconds: LiveData<Long> = _durationInSeconds

        private val _progressMax: MutableLiveData<Long> = MutableLiveData(0)
        val progressMax: LiveData<Long> = _progressMax

        fun loadData(){
            uiScope.launch {
                loadUsers()
                setSubtask()
                initializeTimer()
            }
        }

        fun startTask() {
            startForeground(Constants.NOTIFICATION_TASK_TIMER_ID, baseNotificationBuilder.build())
        }

        fun finishTask() {
            stopForeground(true)
            stopSelf()
        }

        fun saveTaskIntoHistory() {
            lifecycleScope.launch {
                withContext(Dispatchers.IO){
                    taskDao.insertDatedTask(DoneTask(date = Calendar.getInstance().timeInMillis, taskId = taskId))
                }
            }
        }

        private suspend fun loadUsers() {
            withContext(Dispatchers.IO) {
                subtasks = subtasksDao.getAllSubtasks(taskId)
                        as MutableList<Subtask>
                if(subtasks.isEmpty())
                    subtasks.add(  Subtask(taskDao.get(taskId)!!))
            }
        }

        private fun anyTasksLeft(): Boolean = subtaskIndex < subtasks.lastIndex

        private fun nextSubtask() {
            subtaskIndex += 1
        }

        private fun resetRepCount() {
            _subtaskRepCount.value = 0
        }

        private fun setSubtaskRepToInitialState() {
            initializeTimer()
        }

        private fun initializeTimer() {
            if(currentSubtask.value!!.breakInterval > 0L) {
                initializeBreakTimer()
            } else {
                initializeSubtaskTimer()
            }
        }

        private fun initializeBreakTimer() {
            _progressMax.value = currentSubtask.value!!.breakInterval
            updateDuration(_currentSubtask.value?.breakInterval)
            timer = CountDownSecondsTimerWithState(durationInSeconds.value ?: 0,
                ::updateDuration,
                ::startSubtaskTimer)
        }

        private fun initializeSubtaskTimer() {
            _progressMax.value = currentSubtask.value!!.duration
            updateDuration(_currentSubtask.value?.duration)
            timer = CountDownSecondsTimerWithState(
                durationInSeconds.value  ?: 0,
                ::updateDuration,
                ::subtaskRepFinished)
        }

        private fun startSubtaskTimer() {
            initializeSubtaskTimer()
            timer?.start()
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
                timer?.start()
            else
                _state.value = pausedState
        }

        private fun subtaskRepDone() {
            _subtaskRepCount.value = _subtaskRepCount.value?.plus(1)
        }

        private fun allSubtaskRepsDone(): Boolean = currentSubtask.value?.count == subtaskRepCount.value

        private fun automaticPlayback(): Boolean = _currentSubtask.value?.playAutomatically == true

        private fun setSubtask() {
            _currentSubtask.value = subtasks[subtaskIndex]
            resetRepCount()
            setSubtaskRepToInitialState()
        }

        fun newSubtask() {
            if(anyTasksLeft()) {
                nextSubtask()
                setSubtask()
            }
            else {
                if(state == startState) _skippedAllTasks.value = true
                else {
                    saveTaskIntoHistory()
                    _allTasksDone.value = true
                }

                finishTask()
            }
        }
    }

}