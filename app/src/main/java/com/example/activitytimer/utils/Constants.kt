package com.example.activitytimer.utils

object Constants {
    const val RUNNING_DATABASE_NAME = "running_db"

    const val REQUEST_CODE_LOCATION_PERMISSION = 0

    const val ACTION_START_OR_RESUME_SERVICE = "ACTION_START_OR_RESUME_SERVICE"
    const val ACTION_PAUSE_SERVICE = "ACTION_PAUSE_SERVICE"
    const val ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE"
    const val ACTION_SHOW_TRACKING_FRAGMENT = "ACTION_SHOW_TRACKING_FRAGMENT"
    const val ACTION_TRACK_INTERVAL = "ACTION_TRACK_INTERVAL"
    const val ACTION_INITIALIZE_CURRENT_SUBTASK = "ACTION_INITIALIZE_CURRENT_SUBTASK"
    const val ACTION_TASK_TIMER_SKIP = "ACTION_TASK_TIMER_SKIP"
    const val ACTION_STOP_SERVICE_BEFOREHAND = "ACTION_STOP_SERVICE_BEFOREHAND"

    const val NOTIFICATION_TRACKING_CHANNEL_ID = "tracking_channel"
    const val NOTIFICATION_TRACKING_CHANNEL_NAME = "Activity Tracking"
    const val NOTIFICATION_TRACKING_TIMER_ID = 1
    const val NOTIFICATION_TASK_TIMER_ID = 2


    const val TIMER_UPDATE_INTERVAL = 50L

    val categories = listOf("Work", "Study", "Health", "Sport",
        "Hobby", "Household chores", "Playing", "Other")
    val CATEGORY_OTHER_INDEX: Int get() = categories.lastIndex
}