package com.example.activitytimer.screens.createTask

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.activitytimer.screens.createTask.viewModels.CreateSubtaskViewModel
import com.example.activitytimer.databinding.FragmentCreateSubtaskBinding
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@ExperimentalTime
class CreateSubtaskFragment : Fragment() {
    private lateinit var binding: FragmentCreateSubtaskBinding
    private val viewModel: CreateSubtaskViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateSubtaskBinding.inflate(inflater)
        binding.viewModel = viewModel

        val navController = findNavController()
        val isTimeTracking: Boolean = CreateSubtaskFragmentArgs.fromBundle(requireArguments()).duration != -1L

        if(isTimeTracking) {
            setupView()

            binding.buttonLabelAllLater.setOnClickListener {
                navController.previousBackStackEntry?.savedStateHandle?.set("labelAllLater", true)
                navController.popBackStack()
            }

            binding.buttonLabelLater.setOnClickListener {
                navController.popBackStack()
            }
        }

        viewModel.subtaskSaved.observe(viewLifecycleOwner) { subtaskSaved ->
            if (subtaskSaved) {
                Log.d("BugH", "before click")
                readInput()
                navController.popBackStack()
                Log.d("BugH", "after click")
            }
        }

        binding.editTextTime.setOnClickListener {
            DurationPickerDialogFragment().show(parentFragmentManager, null)
        }

        setFragmentResultListener("durationBundle") { _, bundle ->
            val hours = Duration.hours(bundle.getInt("hours"))
            val minutes = Duration.minutes(bundle.getInt("minutes"))
            val seconds = Duration.seconds(bundle.getInt("seconds"))
            val duration = hours.plus(minutes).plus(seconds)
            viewModel.subtask.time = duration.inWholeSeconds
        }

        return binding.root
    }

    private fun readInput() {
        viewModel.subtask.count = binding.editTextNumber.text.toString().toInt()
        viewModel.subtask.playAutomatically = binding.creationChbAutomaticPlay.isChecked
        viewModel.subtask.breakInterval = binding.editTextBreakInterval.text.toString().toLong()
    }

    private fun setupView() {
        binding.buttonLabelAllLater.visibility = View.VISIBLE
        binding.buttonLabelLater.visibility = View.VISIBLE

        viewModel.subtask.time = CreateSubtaskFragmentArgs.fromBundle(requireArguments()).duration
    }
}