package com.example.activitytimer.taskList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.activitytimer.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SubtaskListFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_subtask_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
/*        val executeTaskButton = view.findViewById<FloatingActionButton>(R.id.fab)
        executeTaskButton.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_SubtaskList_to_TaskExecution)
        )
        super.onViewCreated(view, savedInstanceState)*/
    }

}