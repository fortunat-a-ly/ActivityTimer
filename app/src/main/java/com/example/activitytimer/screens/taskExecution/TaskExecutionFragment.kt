package com.example.activitytimer.screens.taskExecution

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.activitytimer.R
import com.example.activitytimer.databinding.FragmentTaskExecutionBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.time.Duration.Companion.seconds

@AndroidEntryPoint
class TaskExecutionFragment : Fragment() {
    private lateinit var binding: FragmentTaskExecutionBinding
    private val viewModel: TaskExecutionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTaskExecutionBinding.inflate(inflater, container, false)

        viewModel.allTasksDone.observe(viewLifecycleOwner) {
            if(it) findNavController().navigate(R.id.action_TaskExecution_to_TaskDone)
        }

        viewModel.currentSubtask.observe(viewLifecycleOwner) {
            it?.let{
                binding.taskTxvName.text = it.name
                adaptFontSize(it.duration.seconds.toString().length)
                binding.executionProgressBarTimer.max = it.duration.toInt()
                binding.executionProgressBarTimer.progress =
                    viewModel.durationInSeconds.value!!.toInt()
                // binding.taskTxvSetsNumber.text = resources.getString(R.string.reps_count_of_all, viewModel.subtaskRepCount, it.count)
            }
        }

        viewModel.durationInSeconds.observe(viewLifecycleOwner) {
            binding.executionProgressBarTimer.progress = it.toInt()
            adaptFontSize(it.seconds.toString().length)
        }

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    private fun adaptFontSize(textLength: Int) {
        when {
            textLength < 4f -> binding.taskTxvTime.textSize = 50f
            textLength < 7f -> binding.taskTxvTime.textSize = 40f
            else -> binding.taskTxvTime.textSize = 360f / textLength
        }
    }
}