package com.example.activitytimer.screens.taskExecution

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.activitytimer.R
import com.example.activitytimer.data.TaskDatabase
import com.example.activitytimer.databinding.FragmentTaskExecutionBinding

class TaskExecutionFragment : Fragment() {
    private lateinit var binding: FragmentTaskExecutionBinding
    private lateinit var viewModelFactory: TaskExecutionViewModelFactory
    private lateinit var viewModel: TaskExecutionViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTaskExecutionBinding.inflate(inflater, container, false)

        val application = requireNotNull(this.activity).application
        val dataSource = TaskDatabase.getInstance(application).subtaskDatabaseDao

        val taskId = TaskExecutionFragmentArgs.fromBundle(requireArguments()).taskId

        viewModelFactory = TaskExecutionViewModelFactory(dataSource, taskId, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(TaskExecutionViewModel::class.java)

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