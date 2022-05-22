package com.example.activitytimer.screens.timer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.activitytimer.databinding.FragmentTimerBinding
import com.example.activitytimer.utils.Constants
import com.example.activitytimer.utils.DurationString
import com.example.activitytimer.utils.setIcon
import kotlin.time.Duration.Companion.milliseconds

class TimerFragment : Fragment() {

    private lateinit var navController: NavController
    private lateinit var binding: FragmentTimerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTimerBinding.inflate(inflater, container, false)
        navController = findNavController()

        binding.buttonStart.setOnClickListener {
            if(TimerService.isTracking.value!!)
                sendCommandToService(Constants.ACTION_PAUSE_SERVICE)
            else
                startTimerService()
        }

        binding.buttonSave.setOnClickListener {
            if(TimerService.timeRunInMillis.value!!.milliseconds.inWholeSeconds > 0L) {
                sendCommandToService(Constants.ACTION_PAUSE_SERVICE)
                if(TimerService.trackedSubtasks.isNotEmpty() && TimerService.interval.milliseconds.inWholeSeconds > 0L)
                    trackSubtask()
                else
                    navController.navigate(TimerFragmentDirections.actionTimerToCreateTask(true))
            }
        }

        binding.buttonSubtask.setOnClickListener {
            trackSubtask()
        }

        TimerService.isTracking.observe(viewLifecycleOwner) {
            binding.buttonStart.setIcon(TimerService.isTracking.value!!)
        }

        TimerService.timeRunInMillis.observe(viewLifecycleOwner) {
            binding.txvTimer.text = DurationString.fromMilliseconds(it)
        }

        return binding.root
    }

    private fun trackSubtask() {
        if (TimerService.interval.milliseconds.inWholeSeconds > 0L) {
            sendCommandToService(Constants.ACTION_PAUSE_SERVICE)
            navController.navigate(TimerFragmentDirections.actionTimerToCreateSubtask(true))
        }
    }

    override fun onResume() {
        super.onResume()
        binding.txvTimer.text = DurationString.fromMilliseconds(TimerService.timeRunInMillis.value!!)
    }

    private fun startTimerService() {
        val intent = Intent(context, TimerService::class.java) // Build the intent for the service
        Log.d("SER1", intent.toString())
        requireContext().startService(intent)
    }

    private fun sendCommandToService(action: String) =
        Intent(requireContext(), TimerService::class.java).also {
            it.action = action
            requireContext().startService(it)
        }
}