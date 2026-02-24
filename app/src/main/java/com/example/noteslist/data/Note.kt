package com.example.noteslist.data

data class Note(
    val title: String,
    val description: String,
    val isImportant: Boolean,
    val date: String
)