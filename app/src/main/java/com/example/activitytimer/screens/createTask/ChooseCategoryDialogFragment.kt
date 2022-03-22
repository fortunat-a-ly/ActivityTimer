package com.example.activitytimer.screens.createTask

import android.app.Dialog
import android.app.SearchManager
import android.app.Service
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.DialogFragment
import com.example.activitytimer.R
import com.example.activitytimer.SearchableActivity


class ChooseCategoryDialogFragment : DialogFragment() {

    private val categories = arrayOf("health", "job", "study", "sport", "shopping")
    private var checkedId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_choose_category, container)

/*        val searchManager = getSystemService(requireContext(), SearchManager::class.java)
        view.findViewById<SearchView>(R.id.search_view)
            .setSearchableInfo(searchManager?.getSearchableInfo())
            .setIconifiedByDefault(false)*/
        return view
    }

/*    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            AlertDialog.Builder(it)
                .setView(layoutInflater.inflate(R.layout.dialog_choose_category, null))
                .setSingleChoiceItems(categories, -1) { dialog, id ->
                    dialog.dismiss()
                    checkedId = id
                }
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }*/
}