package com.example.todolist.domain

data class Task(val id: Long,
                val content: String,
                val done: Boolean)