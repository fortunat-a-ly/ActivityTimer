package com.example.activitytimer.listScreens

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.example.activitytimer.BR
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.activitytimer.data.ITask

class TaskListAdapter(private val onClick: TaskListener, private val layoutId: Int) :
    ListAdapter<ITask, TaskListAdapter.ViewHolder>(TaskDiffCallback) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        holder.bind(item, onClick)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent, layoutId)
    }

    class ViewHolder private constructor(private val binding: ViewDataBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ITask, taskListener: TaskListener) {
            binding.setVariable(BR.task, item)
            binding.setVariable(BR.taskListener, taskListener)
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup, layoutId: Int): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding: ViewDataBinding = DataBindingUtil.inflate(layoutInflater, layoutId, parent, false)

                return ViewHolder(binding)
            }
        }
    }

}

object TaskDiffCallback : DiffUtil.ItemCallback<ITask>() {
    override fun areItemsTheSame(oldItem: ITask, newItem: ITask): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ITask, newItem: ITask): Boolean {
        return oldItem == newItem
    }
}