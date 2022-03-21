package com.example.activitytimer.screens.createTask

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.activitytimer.R
import com.example.activitytimer.SearchableActivity
import com.example.activitytimer.screens.createTask.viewModels.CreateTaskViewModel
import com.example.activitytimer.screens.createTask.viewModels.CreateTaskViewModelFactory
import com.example.activitytimer.data.subtask.SubtaskDatabaseDao
import com.example.activitytimer.data.TaskDatabase
import com.example.activitytimer.databinding.FragmentCreateTaskBinding
import com.example.activitytimer.data.ITask
import com.example.activitytimer.screens.listScreens.TaskListAdapter
import com.example.activitytimer.screens.listScreens.TaskListener

class CreateTaskFragment : Fragment() {
    private lateinit var binding: FragmentCreateTaskBinding
    private lateinit var viewModel: CreateTaskViewModel
    private lateinit var subtaskSource: SubtaskDatabaseDao
    private lateinit var navController: NavController

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

        navController = findNavController()

        // Get a reference to the ViewModel associated with this fragment.
        viewModel = ViewModelProvider(
                this, viewModelFactory).get(CreateTaskViewModel::class.java)

        //findNavController().currentBackStackEntry!!.saved StateHandle.get<Task>("df")
        binding.viewModel = viewModel

        binding.floatingActionButton.setOnClickListener{
            navController.navigate(R.id.action_CreateTask_to_CreateSubtask)
        }

        binding.buttonSave.setOnClickListener {
            viewModel.saveToDatabase()
            navController.popBackStack()
        }

        binding.editTextCategory.setOnClickListener {
           // ChooseCategoryDialogFragment().show(requireFragmentManager(), "smth")
            startActivity(Intent(context, SearchableActivity::class.java))
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

}