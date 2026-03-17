package com.example.noteslist.data.dto

data class NoteDto(
    val id: Long,
    val title: String,
    val description: String,
    val isImportant: Boolean,
    val date: String,
    val isRead: Boolean = false
)
