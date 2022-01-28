package com.example.activitytimer.createTask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.activitytimer.R
import com.example.activitytimer.data.task.Task
import com.example.activitytimer.data.task.TaskDatabase
import com.example.activitytimer.databinding.FragmentCreateTaskBinding

class CreateTaskFragment : Fragment() {
    private lateinit var binding: FragmentCreateTaskBinding
    private lateinit var viewModel: CreateTaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateTaskBinding.inflate(inflater)

        val application = requireNotNull(this.activity).application
        // Create an instance of the ViewModel Factory.
        val dataSource = TaskDatabase.getInstance(application).taskDatabaseDao
        val viewModelFactory = CreateTaskViewModelFactory(dataSource, this, application)

        // Get a reference to the ViewModel associated with this fragment.
        viewModel = ViewModelProvider(
                this, viewModelFactory).get(CreateTaskViewModel::class.java)

        //findNavController().currentBackStackEntry!!.saved StateHandle.get<Task>("df")
        binding.viewModel = viewModel

        binding.floatingActionButton.setOnClickListener{
            findNavController().navigate(R.id.action_CreateTask_to_CreateSubtask)
        }

        return binding.root
    }
}