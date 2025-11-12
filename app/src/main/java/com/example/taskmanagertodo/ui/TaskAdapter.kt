package com.example.taskmanagertodo.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanager.databinding.ItemTaskBinding
import com.example.taskmanagertodo.data.Task

/**
 * Adapter for RecyclerView. Provides callback for edit/delete actions.
 */
class TaskAdapter(
    private var items: List<Task>,
    private val listener: Listener
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    interface Listener {
        fun onEdit(task: Task)
        fun onDelete(task: Task)
    }

    inner class TaskViewHolder(val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(task: Task) {
            binding.tvTitle.text = task.title
            binding.tvDescription.text = task.description
            binding.btnEdit.setOnClickListener { listener.onEdit(task) }
            binding.btnDelete.setOnClickListener { listener.onDelete(task) }
        }
    }

    fun updateList(newList: List<Task>) {
        items = newList
        notifyDataSetChanged() // for simplicity; consider DiffUtil for production
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}