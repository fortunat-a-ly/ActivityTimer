package com.fortunately.timepass.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.fortunately.timepass.R
import com.fortunately.timepass.screens.doneTasks.DoneTaskList

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