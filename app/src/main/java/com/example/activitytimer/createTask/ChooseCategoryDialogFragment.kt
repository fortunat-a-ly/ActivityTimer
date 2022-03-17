package com.example.activitytimer.createTask

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.activitytimer.R


class ChooseCategoryDialogFragment : DialogFragment() {

    private val categories = arrayOf("health", "job", "study", "sport", "shopping")
    private var checkedId: Int = -1

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            AlertDialog.Builder(it)
                .setSingleChoiceItems(categories, -1) { dialog, id ->
                    dialog.dismiss()
                    checkedId = id
                }
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}