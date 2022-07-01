package com.example.activitytimer.screens.doneTasks

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.activitytimer.data.DatedTask
import com.example.activitytimer.utils.ComposeTheme
import dagger.hilt.android.AndroidEntryPoint
import java.sql.Date
import kotlin.time.Duration.Companion.seconds

@AndroidEntryPoint
class DoneTaskList : Fragment() {
    private val viewModel: DoneTasksViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val darkTheme = when (resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> true
            else -> false
        }
        return ComposeView(requireContext()).apply {
            viewModel.list.observe(viewLifecycleOwner) { list ->
                setContent {
                    MaterialTheme(colors = if (darkTheme) ComposeTheme.DarkColors else ComposeTheme.LightColors) {
                        DoneTaskList(list.groupBy { Date(it.datedRecord.date).toString() })
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun DoneTaskList(grouped: Map<String, List<DatedTask>>) {
        LazyColumn (modifier = Modifier
            .background(color = MaterialTheme.colors.background)) {
            grouped.forEach { (date, tasks) ->
                stickyHeader {
                    DateHeader(date)
                }

                items(tasks.size) { i ->
                    Task(tasks[i])
                }
            }
        }
    }

    @Composable
    fun DateHeader(date: String) {
        Text(date,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colors.primary)
                .padding(vertical = 5.dp, horizontal = 20.dp),
            fontSize = 14.sp
        )
    }

    @Composable
    fun Task(task: DatedTask) {
        Card(
            shape = RoundedCornerShape(15.dp), elevation = 7.dp, modifier = Modifier
                .padding(15.dp, 7.dp),
            border = BorderStroke(1.dp, MaterialTheme.colors.primary)
        ) {
            Row(modifier = Modifier
                .padding(all = 20.dp)) {
                Text(task.task.name, Modifier.weight(1f))
                Text(task.task.duration.seconds.toString())
            }
        }
    }
}