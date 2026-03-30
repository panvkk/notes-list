package com.example.noteslist.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DetailsUiState(
    val noteId: Long?,
    val noteTitle: String,
    val noteDescription: String,
    val isNoteImportant: Boolean
) : Parcelable
