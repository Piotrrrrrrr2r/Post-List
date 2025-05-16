package com.example.postlist.data.model

data class ToDo(
    val userId: Int,
    val id: Int,
    val title: String,
    val completed: Boolean
)