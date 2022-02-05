package com.example.activitytimer.listScreens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.activitytimer.R
import com.example.activitytimer.data.DatabaseDao
import com.example.activitytimer.data.ITask
import com.example.activitytimer.data.TaskDatabase
import com.example.activitytimer.databinding.FragmentTaskListBinding

class SubtaskListFragment : Fragment() {
    private lateinit var binding: FragmentTaskListBinding
    private lateinit var viewModel: TaskListViewModel<*>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTaskListBinding.inflate(inflater)
        binding.fab.background = (ResourcesCompat.getDrawable(resources, R.drawable.ic_execute_task, null))

        val application = requireNotNull(this.activity).application
        val dataSource = TaskDatabase.getInstance(application).subtaskDatabaseDao

        val taskId: Long = 1L

        val viewModelFactory = SubtaskListViewModelFactory(dataSource, taskId, application)
        val viewModel = ViewModelProvider(this, viewModelFactory)
            .get(TaskListViewModel::class.java)

        binding.fab
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView: RecyclerView = binding.taskList
        val adapter = TaskListAdapter (TaskListener { }, R.layout.list_item_subtask)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        viewModel.tasks.observe(viewLifecycleOwner) { adapter.submitList(it) }
/*        val executeTaskButton = view.findViewById<FloatingActionButton>(R.id.fab)
        executeTaskButton.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_SubtaskList_to_TaskExecution)
        )
        super.onViewCreated(view, savedInstanceState)*/
    }

}