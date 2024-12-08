package com.example.todoadrikat.detail

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todoadrikat.detail.ui.theme.TodoAdriKatTheme
import com.example.todoadrikat.list.Task
import com.example.todoadrikat.list.TaskListFragment.Companion.TASK_KEY
import java.util.UUID

class DetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TodoAdriKatTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Detail(
                        initialTask = intent.getSerializableExtra(TASK_KEY) as Task?,
                        onValidate = { newTask: Task ->
                            intent.putExtra(TASK_KEY, newTask)
                            setResult(RESULT_OK, intent)
                            finish()
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Detail(initialTask: Task?, onValidate: (Task) -> Unit, modifier: Modifier = Modifier) {
    val newTask = Task(id=UUID.randomUUID().toString(), title="New task!")
    var task by remember {
        mutableStateOf(initialTask?: newTask)
    }

    Column (
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = task.id
        )
        OutlinedTextField(
            label = {
                Text(text = "Task Title")
            },
            placeholder = {
                Text(text = "Task Title")
            },
            value = task.title,
            onValueChange = {
                task = task.copy(title = it)
            }
        )
        OutlinedTextField(
            label = {
                Text(text = "Task Description")
            },
            placeholder = {
                Text(text = "Task Description")
            },
            value = task.description,
            onValueChange = {
                task = task.copy(description = it)
            },
        )
        Button (
            onClick = {
                onValidate(task)
            }
        )
        {
            Text(
                text = "Done",
                style = MaterialTheme.typography.headlineSmall,
                modifier = modifier
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailPreview() {
    TodoAdriKatTheme {
    }
}