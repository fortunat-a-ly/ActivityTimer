package com.example.activitytimer.screens.createTask

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.clearFragmentResultListener
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.activitytimer.R
import com.example.activitytimer.data.ITask
import com.example.activitytimer.databinding.FragmentCreateTaskBinding
import com.example.activitytimer.screens.createTask.viewModels.CreateTaskViewModel
import com.example.activitytimer.screens.listScreens.TaskListAdapter
import com.example.activitytimer.screens.listScreens.TaskListener
import com.example.activitytimer.screens.timer.TimerService
import com.example.activitytimer.utils.Constants
import com.example.activitytimer.utils.DurationString
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateTaskFragment : Fragment() {
    private lateinit var binding: FragmentCreateTaskBinding
    private val viewModel: CreateTaskViewModel by viewModels()
    private lateinit var navController: NavController
    private lateinit var adapter: TaskListAdapter

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
                Log.d("qwe", "here")
                viewModel.saveToDatabase()
                navController.previousBackStackEntry?.savedStateHandle?.set("taskSaved", true)

                Log.d("qwe", CreateTaskFragmentArgs.fromBundle(requireArguments()).isTracked.toString())

                if(viewModel.timeTracked) {
                    Log.d("qwe", "true")
                    Intent(requireContext(), TimerService::class.java).also {
                        it.action = Constants.ACTION_STOP_SERVICE
                        requireContext().startService(it)
                    }
                }

                navController.popBackStack()
            }
            else {
                Toast.makeText(context, "No subtasks created or no duration entered", Toast.LENGTH_LONG).show()
            }
        }

        binding.editTextDuration.setOnClickListener {
            clearFragmentResultListener("durationBundle")
            setFragmentResultListener("durationBundle") { _, bundle ->
                val durationSeconds = bundle.getLong("seconds")
                if(durationSeconds > 0L) {
                    viewModel.task.duration = durationSeconds
                    binding.editTextDuration.setText(DurationString.fromSeconds(durationSeconds))
                }
            }
            DurationPickerDialogFragment().show(parentFragmentManager, null)
        }

        adapter = TaskListAdapter (TaskListener { }, R.layout.list_item_subtask)
        binding.creationRcvSubtaskList.adapter = adapter
        binding.creationRcvSubtaskList.layoutManager = LinearLayoutManager(context)

        val items = listOf("Work", "Study", "Health", "Sport", "Hobby", "Household chores", "Playing")
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item_category, items)
        (binding.creationCategoryList as? AutoCompleteTextView)?.setAdapter(adapter)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        updateView()
    }

    private fun updateView() {
        adapter.submitList(viewModel.subtasks as List<ITask>?)

        // you can enter task duration if u have no subtasks
        binding.textInputDuration.visibility = when(viewModel.subtasks.isEmpty()) {
            true -> {
                if(viewModel.task.duration > 0L)
                    binding.editTextDuration.setText(
                        DurationString.fromSeconds(viewModel.task.duration))
                View.VISIBLE
            }
            else -> View.GONE
        }

    }

}