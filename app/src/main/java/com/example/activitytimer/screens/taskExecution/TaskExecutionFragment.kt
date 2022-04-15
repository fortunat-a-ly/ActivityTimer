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
                // binding.taskTxvSetsNumber.text = resources.getString(R.string.reps_count_of_all, viewModel.subtaskRepCount, it.count)
            }
        }

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }
}