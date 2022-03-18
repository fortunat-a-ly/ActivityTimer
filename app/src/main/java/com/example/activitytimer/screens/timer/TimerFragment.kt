package com.example.activitytimer.screens.timer

import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import com.example.activitytimer.R
import com.example.activitytimer.databinding.FragmentTimerBinding

class TimerFragment : Fragment() {

    private lateinit var viewModel: TimerViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentTimerBinding.inflate(inflater, container, false)
            //inflater.inflate(R.layout.fragment_timer, container, false)

        val viewModelFactory = TimerViewModelFactory(binding.simpleChronometer)
        viewModel = ViewModelProvider(this, viewModelFactory).get(TimerViewModel::class.java)

        binding.buttonStart.setOnClickListener{
            // Log.d("Tag123", viewModel.base.toString())
            // Log.d("Tag123", viewModel.milliSeconds.toString())
            viewModel.chronometer?.start()
        }

        val navController = findNavController()

        binding.buttonPause.setOnClickListener{
            viewModel.chronometer?.pause()
        }

        binding.buttonSave.setOnClickListener {
            viewModel.chronometer?.pause()
            navController.navigate(TimerFragmentDirections.actionTimerToCreateTask(viewModel.chronometer?.milliSeconds ?: 0))
        }

        binding.buttonSubtask.setOnClickListener {
            viewModel.chronometer?.breakInterval()

            if(!viewModel.labelAllLater)
                viewModel.chronometer?.pause()
                navController.navigate(TimerFragmentDirections.actionTimerToCreateSubtask(viewModel.chronometer?.intervals?.last() ?: 0))
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.labelAllLater = findNavController().currentBackStackEntry?.savedStateHandle?.get<Boolean>("labelAllLater") ?: false
    }
}