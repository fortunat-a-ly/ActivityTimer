package com.fortunately.timepass.screens.createTask

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.fortunately.timepass.R
import com.fortunately.timepass.databinding.DialogDurationPickerBinding
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class DurationPickerDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val binding = DialogDurationPickerBinding.inflate(layoutInflater)
            binding.secondPicker.maxValue = 59
            binding.minutePicker.maxValue = 59
            binding.hourPicker.maxValue = 24
            AlertDialog.Builder(it)
                // Pass null as the parent view because its going in the dialog layout
                .setView(binding.root)
                .setPositiveButton(R.string.saveTask,
                    DialogInterface.OnClickListener { _, _ ->
                        val hours = binding.hourPicker.value.hours
                        val minutes = binding.minutePicker.value.minutes
                        val seconds = binding.secondPicker.value.seconds
                        setFragmentResult("durationBundle",
                            bundleOf("seconds" to
                                hours.plus(minutes).plus(seconds).inWholeSeconds)
                        )
                    })
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}