package com.fortunately.timepass.screens.createTask

import android.content.Intent
import android.os.Bundle
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
import com.fortunately.timepass.R
import com.fortunately.timepass.data.ITask
import com.fortunately.timepass.databinding.FragmentCreateTaskBinding
import com.fortunately.timepass.screens.createTask.viewModels.CreateTaskViewModel
import com.fortunately.timepass.screens.listScreens.TaskListAdapter
import com.fortunately.timepass.screens.listScreens.TaskListener
import com.fortunately.timepass.screens.timer.TimerService
import com.fortunately.timepass.utils.Constants
import com.fortunately.timepass.utils.DurationString
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
                val category = binding.creationCategoryList.text.toString()
                if(category.isNotEmpty())
                    viewModel.task.category = Constants.categories.indexOf(category)
                viewModel.saveToDatabase()
                navController.previousBackStackEntry?.savedStateHandle?.set("taskSaved", true)

                if(viewModel.timeTracked) {
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

        val adapter = ArrayAdapter(requireContext(), R.layout.list_item_category, Constants.categories)
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