package com.example.activitytimer.ui.main

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.activitytimer.R

class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int = 2

    override fun getItem(position: Int): Fragment {
        Log.d("ViewP", "getItem")
        return when(position) {
            0 -> Fragment(R.layout.content_main)
            1 -> Fragment(R.layout.content_timer)
            else -> Fragment(R.layout.content_main)
        }
    }

}