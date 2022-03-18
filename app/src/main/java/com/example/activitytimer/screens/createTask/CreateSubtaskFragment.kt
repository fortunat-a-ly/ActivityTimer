package com.example.activitytimer.screens.createTask

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.activitytimer.screens.createTask.viewModels.CreateSubtaskViewModel
import com.example.activitytimer.databinding.FragmentCreateSubtaskBinding

class CreateSubtaskFragment : Fragment() {
    private lateinit var binding: FragmentCreateSubtaskBinding
    private lateinit var viewModel: CreateSubtaskViewModel

    private val isTimeTracking: Boolean = CreateSubtaskFragmentArgs.fromBundle(requireArguments()).duration != -1L

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateSubtaskBinding.inflate(inflater)
        viewModel = ViewModelProvider(this).get(CreateSubtaskViewModel::class.java)
        binding.viewModel = viewModel

        val navController = findNavController()

        if(isTimeTracking) {
            setupView()

            binding.buttonLabelAllLater.setOnClickListener {
                navController.previousBackStackEntry?.savedStateHandle?.set("labelAllLater", true)
                navController.popBackStack()
            }

            binding.buttonLabelLater.setOnClickListener {
                navController.popBackStack()
            }
        }

        viewModel.subtaskSaved.observe(viewLifecycleOwner) { subtaskSaved ->
            if (subtaskSaved) {
                Log.d("BugH", "before click")
                readInput()
                navController.popBackStack()
                Log.d("BugH", "after click")
            }
        }

        return binding.root
    }

    private fun readInput() {
        // viewModel.subtask.time = binding.editTextTime.text.toString().toLong()
        //viewModel.subtask.count = binding.editTextNumber.text.toString().toInt()
        //viewModel.subtask.playAutomatically = binding.creationChbAutomaticPlay.isChecked
        //viewModel.subtask.breakInterval = binding.editTextBreakInterval.text.toString().toLong()
    }

    private fun setupView() {
        binding.buttonLabelAllLater.visibility = View.VISIBLE
        binding.buttonLabelLater.visibility = View.VISIBLE

        viewModel.subtask.time = CreateSubtaskFragmentArgs.fromBundle(requireArguments()).duration
    }
}