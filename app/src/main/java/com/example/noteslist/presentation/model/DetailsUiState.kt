package com.example.noteslist.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DetailsUiState(
    val noteId: Long?,
    val note: ViewTypedModel.Note
) : Parcelable
