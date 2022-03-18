package com.example.activitytimer.screens.timer

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import androidx.core.content.ContextCompat.startForegroundService
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.activitytimer.R
import com.example.activitytimer.databinding.FragmentTimerBinding
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@AndroidEntryPoint
class TimerFragment : Fragment() {

    private lateinit var viewModel: TimerViewModel
    private lateinit var navController: NavController
    private lateinit var chronometer: Chronometer

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentTimerBinding.inflate(inflater, container, false)
        navController = findNavController()

        chronometer = binding.timerChronometer

        viewModel = ViewModelProvider(this).get(TimerViewModel::class.java)

        chronometer.base = viewModel.base

        binding.buttonStart.setOnClickListener {
            startTimer()
        }

        binding.buttonPause.setOnClickListener {
            pauseTimer()
        }

        binding.buttonSave.setOnClickListener {
            pauseTimer()
            navController.navigate(TimerFragmentDirections.actionTimerToCreateTask(viewModel.milliSeconds / 1000))
        }

        binding.buttonSubtask.setOnClickListener {
            breakTimerInterval()
            pauseTimer()
            navController.navigate(TimerFragmentDirections.actionTimerToCreateSubtask(viewModel.intervals.last() / 1000))

            // TODO("workaround")
            if(viewModel.labelAllLater) {
                navController.popBackStack()
            }
        }

        return binding.root
    }

    private fun breakTimerInterval() {
        val breakTime = SystemClock.elapsedRealtime()
        val interval = breakTime - viewModel.subtaskMlSeconds - chronometer.base
        viewModel.subtaskMlSeconds += interval
        viewModel.intervals.add(interval)
    }

    private fun pauseTimer() {
        viewModel.paused = true
        viewModel.milliSeconds = SystemClock.elapsedRealtime() - chronometer.base
        chronometer.stop()
    }

    private fun updateView() {
        chronometer.base = SystemClock.elapsedRealtime() - viewModel.milliSeconds
    }

    private fun startTimer() {
        startTimerService()
        if(viewModel.paused) {
            viewModel.paused = false
            chronometer.base = SystemClock.elapsedRealtime() - viewModel.milliSeconds
            chronometer.start()
        }
    }

    override fun onResume() {
        super.onResume()
        updateSubtaskLabeling()
        updateView()
    }

    private fun updateSubtaskLabeling() {
        viewModel.labelAllLater = navController.currentBackStackEntry?.savedStateHandle?.get<Boolean>("labelAllLater") ?: false
    }

    private fun startTimerService() {
        val appContext = requireActivity().applicationContext

        val intent = Intent(context, TimerService::class.java) // Build the intent for the service
        context?.startForegroundService(intent)
    }
}