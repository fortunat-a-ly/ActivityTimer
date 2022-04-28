package com.example.activitytimer.screens.createTask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.clearFragmentResultListener
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.activitytimer.databinding.FragmentCreateSubtaskBinding
import com.example.activitytimer.screens.createTask.viewModels.CreateSubtaskViewModel
import com.example.activitytimer.screens.timer.TimerService
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
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

        if(CreateSubtaskFragmentArgs.fromBundle(requireArguments()).isTracked) {
            setupTrackedTimeView()
        }

        viewModel.subtaskSaved.observe(viewLifecycleOwner) { subtaskSaved ->
            if (subtaskSaved && readInput()) {

                if (CreateSubtaskFragmentArgs.fromBundle(requireArguments()).isTracked) {
                    TimerService.saveSubtask(viewModel.subtask)
                }
                navController.popBackStack()
            }
        }

        binding.editTextTime.setOnClickListener {
            clearFragmentResultListener("durationBundle")
            setFragmentResultListener("durationBundle") { _, bundle ->
                val hours = bundle.getInt("hours").hours
                val minutes = bundle.getInt("minutes").minutes
                val seconds = bundle.getInt("seconds").seconds
                val duration = hours.plus(minutes).plus(seconds)
                if(duration.inWholeSeconds > 0L) {
                    viewModel.subtask.duration = duration.inWholeSeconds
                    binding.editTextTime.setText(duration.toString())
                }
            }
            DurationPickerDialogFragment().show(parentFragmentManager, null)
        }

        binding.editTextBreakInterval.setOnClickListener {
            clearFragmentResultListener("durationBundle")
            setFragmentResultListener("durationBundle") { _, bundle ->
                val hours = bundle.getInt("hours").hours
                val minutes = bundle.getInt("minutes").minutes
                val seconds = bundle.getInt("seconds").seconds
                val duration = hours.plus(minutes).plus(seconds)
                if(duration.inWholeSeconds > 0) {
                    viewModel.subtask.duration = duration.inWholeSeconds
                    binding.editTextBreakInterval.setText(duration.toString())
                }
            }
            DurationPickerDialogFragment().show(parentFragmentManager, null)
        }

        return binding.root
    }

    private fun readInput() : Boolean {
        val allFieldsEntered = binding.editTextTime.text.trim().isNotEmpty() &&
                binding.editTextName.text.trim().isNotEmpty()

        if(allFieldsEntered) {
            viewModel.subtask.name = binding.editTextName.text.trim().toString()
            viewModel.subtask.playAutomatically = binding.creationChbAutomaticPlay.isChecked
            if(binding.editTextNumber.text.trim().isNotEmpty())
                viewModel.subtask.count = binding.editTextNumber.text.toString().toInt()
            return true
        }

        Toast.makeText(context, "Fill all the fields", Toast.LENGTH_LONG).show()
        return false
    }

    private fun setupTrackedTimeView() {
        viewModel.subtask.duration = TimerService.interval.milliseconds.inWholeSeconds
        binding.editTextTime.setText(viewModel.subtask.duration.seconds.toString())
        binding.editTextTime.isClickable = false
    }
}