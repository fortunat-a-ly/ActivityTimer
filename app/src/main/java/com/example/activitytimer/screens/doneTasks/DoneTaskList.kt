package com.example.activitytimer.screens.doneTasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.activitytimer.databinding.FragmentTaskListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DoneTaskList : Fragment() {
    private lateinit var binding: FragmentTaskListBinding
    private val viewModel: DoneTasksViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTaskListBinding.inflate(inflater)
        binding.fab.visibility = View.GONE
        return binding.root
    }
}