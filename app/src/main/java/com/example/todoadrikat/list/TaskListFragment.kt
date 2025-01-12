package com.example.todoadrikat.list

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import com.example.todoadrikat.R
import com.example.todoadrikat.data.Api
import com.example.todoadrikat.data.TaskListViewModel
import com.example.todoadrikat.detail.DetailActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch
import java.util.UUID

class TaskListFragment : Fragment() {

    private val adapterListener: TaskListListener = object: TaskListListener {
        override fun onClickDelete(task: Task) {
            viewModel.delete(task)
        }

        override fun onClickEdit(task: Task) {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(TASK_KEY, task)
            editTask.launch(intent)
        }
    }
    private val adapter = TaskListAdapter(adapterListener)

    private lateinit var createTask : ActivityResultLauncher<Intent>
    private lateinit var editTask : ActivityResultLauncher<Intent>

    private lateinit var userTextView: TextView

    private val viewModel = TaskListViewModel()

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

        // Set the Add Button to add a task
        val floatingActionButton = rootView.findViewById<FloatingActionButton>(R.id.primaryFloatingActionButton)
        val refreshButton = rootView.findViewById<FloatingActionButton>(R.id.refreshFloatingActionButton)

        val intent = Intent(context, DetailActivity::class.java)
        floatingActionButton.setOnClickListener {
            // On click of the floating button, we start the create task launcher
            intent.removeExtra(TASK_KEY)
            createTask.launch(intent)
        }

        refreshButton.setOnClickListener {
            viewModel.refresh()
        }

        userTextView = rootView.findViewById<TextView>(R.id.userTextView)
        val userImageView: ImageView = rootView.findViewById<ImageView>(R.id.imageView)
        //userImageView.load("https://goo.gl/gEgYUd")


        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.main_recycler_view)
        recyclerView.adapter = adapter

        // Check for incoming data from other apps
        var intent = requireActivity().intent
        if (Intent.ACTION_SEND == intent.action && intent.type == "text/plain") {
            val importedTask = Task(
                id = UUID.randomUUID().toString(),
                title = "New imported task",
                description = intent.getStringExtra(Intent.EXTRA_TEXT) ?: ""
            )
            intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(TASK_KEY, importedTask)
            createTask.launch(intent)
        }

        // Sets up the observer that will automatically refresh the adapter when the list changes
        lifecycleScope.launch {
            viewModel.tasksStateFlow.collect { newList ->
                adapter.currentList = newList
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onResume() {
        super.onResume()


        viewModel.refresh()

        Log.i(null,"Launching coroutine")
        lifecycleScope.launch {
            val userResponse = Api.userWebService.fetchUser()

            Log.i(null, "user Collected !")
            val user = userResponse.body()
            userTextView.text = user?.name
        }
    }

    private fun addNewTask(newTask : Task){
        viewModel.add(newTask)
    }

    private fun updateNewTask(task: Task) {
        viewModel.update(task)
    }

    private fun shareTask(task: Task){
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "${task.title}: ${task.description}")
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    companion object {
        const val TASK_KEY = "task"
    }
}