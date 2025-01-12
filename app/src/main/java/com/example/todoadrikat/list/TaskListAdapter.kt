package com.example.todoadrikat.list

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.todoadrikat.R

interface TaskListListener {
    fun onClickDelete(task: Task)
    fun onClickEdit(task: Task)
}

class TaskListAdapter(val listener: TaskListListener) : RecyclerView.Adapter<TaskListAdapter.TaskViewHolder>() {
    var currentList: List<Task> = emptyList()

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var textView: TextView = itemView.findViewById<TextView>(R.id.task_title)
        private var editButton: ImageButton = itemView.findViewById<ImageButton>(R.id.editTask)
        private var deleteButton: ImageButton = itemView.findViewById<ImageButton>(R.id.deleteTask)

        fun bind(task: Task) {
            textView.setOnLongClickListener {
                // Create the share intent
                val context = it.context
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, "${task.title}: ${task.description}")
                }

                // Start the share intent
                context.startActivity(Intent.createChooser(shareIntent, "Share Task"))

                // Return true to indicate the long-click was handled
                true
            }
            textView.text = task.title
            editButton.setOnClickListener{
                listener.onClickEdit(task)
            }
            deleteButton.setOnClickListener{
                listener.onClickDelete(task)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.fragment_item_task, parent, false)
        return TaskViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return currentList.count()
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}