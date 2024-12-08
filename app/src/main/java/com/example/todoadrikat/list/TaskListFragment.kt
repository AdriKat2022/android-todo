package com.example.todoadrikat.list

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.RecyclerView
import com.example.todoadrikat.R
import com.example.todoadrikat.detail.DetailActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.UUID

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TaskListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TaskListFragment : Fragment() {
    // TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null
    private var taskList = listOf(
        Task("id_1", "Task 1", description = "Description"),
        Task("id_2", "Task 2"),
        Task("id_3", "Task 3")
    )
    private val adapter = TaskListAdapter()
    private lateinit var createTask : ActivityResultLauncher<Intent>
    private lateinit var editTask : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Correctly defines the createTask and editTask activity launchers
        createTask = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result ->
            // Handle task append
            val task = result.data?.getSerializableExtra(TASK_KEY) as Task?

            if (task != null) {
                addNewTask(task)
            }
            else {
                // Warn the user something went wrong
            }
        }
        editTask = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result ->
            // Handle task append
            val task = result.data?.getSerializableExtra(TASK_KEY) as Task?

            if (task != null) {
                updateNewTask(task)
            }
            else {
                // Warn the user something went wrong
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_task_list, container, false)
        adapter.currentList = taskList

        // Set the Add Button to add a task
        val floatingActionButton = rootView.findViewById<FloatingActionButton>(R.id.primaryFloatingActionButton)

        val intent = Intent(context, DetailActivity::class.java)
        floatingActionButton.setOnClickListener {
            // On click of the floating button, we start the create task launcher
            intent.removeExtra(TASK_KEY)
            createTask.launch(intent)
        }

        // Set up onClickEdit
        adapter.onClickEdit = { task: Task ->
            intent.putExtra(TASK_KEY, task)
            editTask.launch(intent)
        }

        // Set up the onClickDelete
        adapter.onClickDelete = { task: Task ->
            taskList -= task
            refreshAdapter()
        }

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.main_recycler_view)
        recyclerView.adapter = adapter
    }

    private fun addNewTask(newTask : Task){
        taskList += newTask
        refreshAdapter()
    }

    private fun updateNewTask(task: Task) {
        // Updates the existing task according to its ID
        taskList = taskList.map {
            if (it.id == task.id) task else it
        }
        refreshAdapter()
    }

    private fun refreshAdapter(){
        adapter.currentList = taskList
        adapter.notifyDataSetChanged()
    }

    companion object {
        const val TASK_KEY = "task"
    }
}