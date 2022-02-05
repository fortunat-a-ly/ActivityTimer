package com.example.activitytimer.taskExecution

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
        // Create an instance of the ViewModel Factory.
        val dataSource = TaskDatabase.getInstance(application).subtaskDatabaseDao

        val taskId = 1L
        viewModelFactory = TaskExecutionViewModelFactory(dataSource, taskId, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(TaskExecutionViewModel::class.java)

        viewModel.subtasks.observe(viewLifecycleOwner) {
            binding.taskTxvSetsNumber.text = it?.size.toString()
        }

        viewModel.allTasksDone.observe(viewLifecycleOwner) {
            if(it) findNavController().navigate(R.id.action_TaskExecution_to_TaskDone)
        }

        binding.viewModel = viewModel
        return binding.root
    }
}