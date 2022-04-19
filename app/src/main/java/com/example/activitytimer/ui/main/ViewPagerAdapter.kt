package com.example.activitytimer.ui.main

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.activitytimer.R
import com.example.activitytimer.screens.doneTasks.DoneTaskList

class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int = 3

    override fun getItem(position: Int): Fragment {
        return when(position) {
            0 -> Fragment(R.layout.content_main)
            1 -> Fragment(R.layout.content_timer)
            2 -> DoneTaskList()
            else -> Fragment(R.layout.content_main)
        }
    }

}