package com.example.noteslist.data


sealed interface ViewTyped {
    data class Note(
        val title: String,
        val description: String,
        val isImportant: Boolean,
        val date: String
    ) : ViewTyped

    data class NoteStack(val notes: List<Note>) : ViewTyped
}