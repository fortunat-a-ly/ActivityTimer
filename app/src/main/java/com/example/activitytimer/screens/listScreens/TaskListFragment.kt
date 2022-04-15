package com.example.activitytimer.screens.listScreens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.activitytimer.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskListFragment : Fragment() {

    private val viewModel: TaskListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return inflater.inflate(R.layout.fragment_task_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.task_list)
        val adapter = TaskListAdapter (TaskListener { taskId ->  listItemOnClick(taskId)}, R.layout.list_item_task)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        viewModel.tasks.observe(viewLifecycleOwner) { it -> adapter.submitList(it) }

        view.findViewById<FloatingActionButton>(R.id.fab).setOnClickListener (
            Navigation.createNavigateOnClickListener(R.id.action_TaskList_to_CreateTask)
        )
    }

    private fun listItemOnClick(taskId: Long) {
        findNavController().navigate(TaskListFragmentDirections.actionTaskListToSubtaskList(taskId))
    }
}