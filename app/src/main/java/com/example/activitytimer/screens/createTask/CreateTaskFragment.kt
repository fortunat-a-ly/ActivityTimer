package com.example.activitytimer.screens.createTask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.activitytimer.R
import com.example.activitytimer.screens.createTask.viewModels.CreateTaskViewModel
import com.example.activitytimer.databinding.FragmentCreateTaskBinding
import com.example.activitytimer.data.ITask
import com.example.activitytimer.screens.listScreens.TaskListAdapter
import com.example.activitytimer.screens.listScreens.TaskListener
import dagger.hilt.android.AndroidEntryPoint
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@AndroidEntryPoint
class CreateTaskFragment : Fragment() {
    private lateinit var binding: FragmentCreateTaskBinding
    private val viewModel: CreateTaskViewModel by viewModels()
    private lateinit var navController: NavController
    private lateinit var adapter: TaskListAdapter

    @OptIn(ExperimentalTime::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateTaskBinding.inflate(inflater)

        navController = findNavController()

        binding.viewModel = viewModel

        binding.floatingActionButton.setOnClickListener{
            navController.navigate(R.id.action_CreateTask_to_CreateSubtask)
        }

        binding.buttonSave.setOnClickListener {
            if(viewModel.canBeSaved) {
                viewModel.saveToDatabase()
                navController.previousBackStackEntry?.savedStateHandle?.set("taskSaved", true)
                navController.popBackStack()
            }
            else {
                Toast.makeText(context, "No subtasks created or no duration entered", Toast.LENGTH_LONG).show()
            }
        }

        binding.editTextCategory.setOnClickListener {
            ChooseCategoryDialogFragment().show(requireFragmentManager(), "smth")
            //startActivity(Intent(context, SearchableActivity::class.java))
        }

        binding.editTextDuration.setOnClickListener {
            DurationPickerDialogFragment().show(parentFragmentManager, null)
        }

        setFragmentResultListener("durationBundle") { _, bundle ->
            val hours = Duration.hours(bundle.getInt("hours"))
            val minutes = Duration.minutes(bundle.getInt("minutes"))
            val seconds = Duration.seconds(bundle.getInt("seconds"))
            val duration = hours.plus(minutes).plus(seconds)
            viewModel.task.duration = duration.inWholeSeconds
            binding.editTextDuration.setText(duration.toString())
        }

        adapter = TaskListAdapter (TaskListener { }, R.layout.list_item_subtask)
        binding.creationRcvSubtaskList.adapter = adapter
        binding.creationRcvSubtaskList.layoutManager = LinearLayoutManager(context)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        updateView()
    }

    private fun updateView() {
        adapter.submitList(viewModel.subtasks as List<ITask>?)

        // you can enter task duration if u have no subtasks
        binding.editTextDuration.visibility = when(viewModel.subtasks.isEmpty()) {
            true -> View.VISIBLE
            else -> View.GONE
        }
    }

}