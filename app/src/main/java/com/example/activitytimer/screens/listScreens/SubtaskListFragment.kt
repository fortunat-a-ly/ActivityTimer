package com.example.activitytimer.screens.listScreens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.activitytimer.R
import com.example.activitytimer.data.ITask
import com.example.activitytimer.databinding.FragmentSubtaskListBinding
import com.example.activitytimer.screens.taskExecution.CountDownTimerService
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SubtaskListFragment : Fragment() {
    private lateinit var binding: FragmentSubtaskListBinding
    private val viewModel: SubtaskListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSubtaskListBinding.inflate(inflater)
        binding.list.fab.setImageResource(R.drawable.ic_execute_task)

        binding.list.fab.setOnClickListener{
/*            if(CountDownTimerService.taskExecuting && CountDownTimerService.taskId != viewModel.taskId)
                Toast.makeText(context, "You have another task in progress", Toast.LENGTH_LONG)
                    .show()
            else*/
                findNavController().navigate(
                    SubtaskListFragmentDirections.actionSubtaskListToTaskExecution(viewModel.taskId))
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView: RecyclerView = binding.list.taskList
        val adapter = TaskListAdapter (TaskListener { taskId -> listItemOnClick(taskId) }, R.layout.list_item_subtask)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        viewModel.subtasks.observe(viewLifecycleOwner) {
            adapter.submitList(it as List<ITask>?)
            // Log.d("Subtasks", viewModel.tasks.value.toString())
            // Log.d("Subtasks", viewModel.tasks.value?.size.toString())
        }

        viewModel.task.observe(viewLifecycleOwner) {
            it?.let {
                binding.listTxvName.text = it.name
                binding.listTxvDuration.text = it.duration.toString()
                binding.listTxvDurationDistribution.text = getString(R.string.duration_distribution, it.duration, it.duration)
                // TODO("above formatted duration")
            }
        }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun listItemOnClick(taskId: Long) {
        val bundle = bundleOf("taskId" to taskId)

        // findNavController().navigate(R.id., bundle)
    }
}