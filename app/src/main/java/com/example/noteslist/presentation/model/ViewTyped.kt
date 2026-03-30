package com.example.noteslist.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
sealed interface ViewTyped : Parcelable {
    data class Note(
        val id: Long,
        val title: String,
        val description: String,
        val isImportant: Boolean,
        val date: String,
        var isRead: Boolean = false
    ) : ViewTyped

    data class NoteStack(val notes: List<Note>) : ViewTyped
    data class DateTitle(val date: String): ViewTyped
}