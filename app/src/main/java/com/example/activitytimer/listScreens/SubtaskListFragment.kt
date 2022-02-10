package com.example.activitytimer.listScreens

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.activitytimer.R
import com.example.activitytimer.data.DatabaseDao
import com.example.activitytimer.data.ITask
import com.example.activitytimer.data.TaskDatabase
import com.example.activitytimer.databinding.FragmentTaskListBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SubtaskListFragment : Fragment() {
    private lateinit var binding: FragmentTaskListBinding
    private lateinit var viewModel: TaskListViewModel<*>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTaskListBinding.inflate(inflater)
        binding.fab.background = (ResourcesCompat.getDrawable(resources, R.drawable.ic_execute_task, null))

        val application = requireNotNull(this.activity).application
        val dataSource = TaskDatabase.getInstance(application).subtaskDatabaseDao

        val taskId: Long = SubtaskListFragmentArgs.fromBundle(requireArguments()).taskId

        val viewModelFactory = SubtaskListViewModelFactory(dataSource, taskId, application)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(TaskListViewModel::class.java)

        binding.fab.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                SubtaskListFragmentDirections.actionSubtaskListToTaskExecution(taskId)
            )
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView: RecyclerView = binding.taskList
        val adapter = TaskListAdapter (TaskListener { taskId -> listItemOnClick(taskId) }, R.layout.list_item_subtask)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        viewModel.tasks.observe(viewLifecycleOwner) {
            adapter.submitList(it)
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