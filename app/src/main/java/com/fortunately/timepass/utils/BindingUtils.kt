package com.fortunately.timepass.utils

import android.widget.Button
import android.widget.ImageButton
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import com.fortunately.timepass.R

@BindingAdapter("text")
fun Button.setText(canBePaused: Boolean) {
    text = if (canBePaused)
        resources.getString(R.string.pause)
    else resources.getString(R.string.start)
}

@BindingAdapter("icon")
fun ImageButton.setIcon(canBePaused: Boolean) {
    val icon = if (canBePaused)
        ResourcesCompat.getDrawable(resources, R.drawable.ic_pause, null)
    else ResourcesCompat.getDrawable(resources, R.drawable.ic_execute_task, null)
    setImageDrawable(icon)
}
