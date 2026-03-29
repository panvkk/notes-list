package com.example.noteslist.data

import java.time.LocalDate


sealed interface ViewTyped {
    data class Note(
        val title: String,
        val description: String,
        val isImportant: Boolean,
        val date: String,
        var isRead: Boolean = false
    ) : ViewTyped

    data class NoteStack(val notes: List<Note>) : ViewTyped
    data class DateTitle(val date: String): ViewTyped
}