package com.example.noteslist.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
sealed interface ViewTypedModel : Parcelable , ViewTyped {
    data class Note(
        val id: Long,
        val title: String,
        val description: String,
        val isImportant: Boolean,
        val date: String,
        var isRead: Boolean = false
    ) : ViewTypedModel {
        @IgnoredOnParcel
        override val uid = "note_${id}"
    }

    data class NoteStack(
        val stackId: Int,
        val notes: List<Note>
    ) : ViewTypedModel {
        @IgnoredOnParcel
        override val uid = "note_${stackId}"
    }
    data class DateTitle(val date: String): ViewTypedModel {
        @IgnoredOnParcel
        override val uid = "note_${date}"
    }
}