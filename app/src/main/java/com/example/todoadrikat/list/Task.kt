package com.example.todoadrikat.list

import java.io.Serializable

data class Task(val id: String, var title: String, var description: String = "") : Serializable