package com.example.noteslist.domain.model

import java.time.LocalDate

data class NoteModel(
    val id: Long,
    val title: String,
    val description: String,
    val date: LocalDate?,
    val isImportant: Boolean,
    val isRead: Boolean
)
