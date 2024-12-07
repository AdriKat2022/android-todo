package com.example.todoadrikat.detail

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todoadrikat.detail.ui.theme.TodoAdriKatTheme

class DetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TodoAdriKatTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Detail(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Detail(modifier: Modifier = Modifier) {
    Column (
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Task Detail",
            style = MaterialTheme.typography.headlineLarge,
            modifier = modifier
        )
        Text(
            text = "Title",
            style = MaterialTheme.typography.headlineMedium,
            modifier = modifier
        )
        Text(
            text = "Description",
            style = MaterialTheme.typography.headlineSmall,
            modifier = modifier
        )
        Button (
            onClick = {}
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
        Detail()
    }
}