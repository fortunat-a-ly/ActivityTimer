package com.example.activitytimer.screens.createTask

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.activitytimer.R
import com.example.activitytimer.screens.createTask.viewModels.CreateTaskViewModel
import com.example.activitytimer.screens.createTask.viewModels.CreateTaskViewModelFactory
import com.example.activitytimer.data.subtask.SubtaskDatabaseDao
import com.example.activitytimer.data.TaskDatabase
import com.example.activitytimer.databinding.FragmentCreateTaskBinding
import com.example.activitytimer.data.ITask
import com.example.activitytimer.screens.listScreens.SubtaskListFragmentArgs
import com.example.activitytimer.screens.listScreens.TaskListAdapter
import com.example.activitytimer.screens.listScreens.TaskListener
import kotlinx.coroutines.*

class CreateTaskFragment : Fragment() {
    private lateinit var binding: FragmentCreateTaskBinding
    private lateinit var viewModel: CreateTaskViewModel
    private lateinit var subtaskSource: SubtaskDatabaseDao

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateTaskBinding.inflate(inflater)

        val application = requireNotNull(this.activity).application
        // Create an instance of the ViewModel Factory.
        val dataSource = TaskDatabase.getInstance(application).taskDatabaseDao
        subtaskSource = TaskDatabase.getInstance(application).subtaskDatabaseDao
        val viewModelFactory = CreateTaskViewModelFactory(dataSource, subtaskSource,this, application)

        // Get a reference to the ViewModel associated with this fragment.
        viewModel = ViewModelProvider(
                this, viewModelFactory).get(CreateTaskViewModel::class.java)

        //findNavController().currentBackStackEntry!!.saved StateHandle.get<Task>("df")
        binding.viewModel = viewModel

        Log.d("ArgsDur", " before")
        val duration = CreateTaskFragmentArgs.fromBundle(requireArguments()).duration

        Log.d("ArgsDur", duration.toString())

        binding.floatingActionButton.setOnClickListener{
            findNavController().navigate(R.id.action_CreateTask_to_CreateSubtask)
        }

        viewModel.subtaskSaved.observe(viewLifecycleOwner) { subtaskSaved ->
            if (subtaskSaved) {
                findNavController().popBackStack()
            }
        }

        binding.editTextCategory.setOnClickListener {
            ChooseCategoryDialogFragment().show(requireFragmentManager(), "smth")
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        val adapter = TaskListAdapter (TaskListener { }, R.layout.list_item_subtask)
        binding.creationRcvSubtaskList.adapter = adapter
        binding.creationRcvSubtaskList.layoutManager = LinearLayoutManager(context)

        adapter.submitList(viewModel.subtasks as List<ITask>?)
    }

    override fun onPause() {
        super.onPause()
        val viewModelJob = Job()
        val uiScope = CoroutineScope(Dispatchers.Main +  viewModelJob)

        uiScope.launch { // launch a new coroutine and continue
            withContext(Dispatchers.IO ) {
                Log.d("Life", subtaskSource.getAll().toString())
            }
        }


    }

    private fun listItemOnClick(taskId: Long) {
        val bundle = bundleOf("taskId" to taskId)

        findNavController().navigate(R.id.action_CreateTask_to_CreateSubtask, bundle)
    }
}