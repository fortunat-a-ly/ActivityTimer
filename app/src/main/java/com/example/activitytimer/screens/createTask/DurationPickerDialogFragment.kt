package com.example.activitytimer.screens.createTask

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.example.activitytimer.R
import com.example.activitytimer.databinding.DialogDurationPickerBinding

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
                        setFragmentResult("durationBundle",
                            bundleOf("hours" to binding.hourPicker.value,
                            "minutes" to binding.minutePicker.value,
                            "seconds" to binding.secondPicker.value)
                        )
                    })
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}