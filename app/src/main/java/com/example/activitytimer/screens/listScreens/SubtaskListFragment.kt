package com.example.activitytimer.screens.listScreens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.activitytimer.R
import com.example.activitytimer.data.ITask
import com.example.activitytimer.databinding.FragmentTaskListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SubtaskListFragment : Fragment() {
    private lateinit var binding: FragmentTaskListBinding
    private val viewModel: SubtaskListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTaskListBinding.inflate(inflater)
        binding.fab.background = (ResourcesCompat.getDrawable(resources, R.drawable.ic_execute_task, null))

        binding.fab.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                SubtaskListFragmentDirections.actionSubtaskListToTaskExecution(viewModel.taskId)
            )
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView: RecyclerView = binding.taskList
        val adapter = TaskListAdapter (TaskListener { taskId -> listItemOnClick(taskId) }, R.layout.list_item_subtask)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        viewModel.subtasks.observe(viewLifecycleOwner) {
            adapter.submitList(it as List<ITask>?)
            // Log.d("Subtasks", viewModel.tasks.value.toString())
            // Log.d("Subtasks", viewModel.tasks.value?.size.toString())
        }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun listItemOnClick(taskId: Long) {
        val bundle = bundleOf("taskId" to taskId)

        // findNavController().navigate(R.id., bundle)
    }
}