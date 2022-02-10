package com.example.activitytimer.createTask

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.activitytimer.createTask.viewModels.CommonClass
import com.example.activitytimer.createTask.viewModels.CreateSubtaskViewModel
import com.example.activitytimer.databinding.FragmentCreateSubtaskBinding


class CreateSubtaskFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentCreateSubtaskBinding.inflate(inflater)
        val viewModel = ViewModelProvider(this).get(CreateSubtaskViewModel::class.java)
        binding.viewModel = viewModel

        val navController = findNavController()

        viewModel.subtaskSaved.observe(viewLifecycleOwner) { subtaskSaved ->
            if (subtaskSaved) {
                readInput(viewModel, binding)
                navController.popBackStack()
            }
        }

        return binding.root
    }

    private fun readInput(viewModel: CreateSubtaskViewModel, binding: FragmentCreateSubtaskBinding) {
        viewModel.subtask.time = binding.editTextTime.text.toString().toLong()
        viewModel.subtask.count = binding.editTextNumber.text.toString().toInt()
    }
}