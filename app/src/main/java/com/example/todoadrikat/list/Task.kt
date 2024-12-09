package com.example.todoadrikat.list

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Task(
    @SerialName("id")
    val id: String,
    @SerialName("content")
    var title: String,
    @SerialName("description")
    var description: String = ""
) : java.io.Serializable