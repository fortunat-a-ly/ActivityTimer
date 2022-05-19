package com.example.activitytimer.screens.taskDone

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.activitytimer.R
import com.example.activitytimer.screens.taskExecution.CountDownTimerService
import com.example.activitytimer.screens.taskExecution.TaskExecutionFragmentArgs
import com.example.activitytimer.utils.Constants.ACTION_STOP_SERVICE

class TaskDoneFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Intent(requireContext(), CountDownTimerService::class.java).also {
            it.action = ACTION_STOP_SERVICE
            requireContext().startService(it)
        }

        return inflater.inflate(R.layout.fragment_task_done, container, false)
    }

}