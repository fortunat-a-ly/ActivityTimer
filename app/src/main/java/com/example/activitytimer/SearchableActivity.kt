package com.example.activitytimer

import android.app.ListActivity
import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat

class SearchableActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.dialog_choose_category)
        //this.setFinishOnTouchOutside(false)

        val searchManager =
            ContextCompat.getSystemService(applicationContext, SearchManager::class.java)

        findViewById<SearchView>(R.id.search_view).apply{
            setSearchableInfo(searchManager?.getSearchableInfo(componentName))
            setIconifiedByDefault(false)
        }

        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                // doMySearch(query)
            }
        }
    }
}