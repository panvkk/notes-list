package com.example.noteslist.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DetailsUiState(
    val noteId: Long?,
    val currentNote: ViewTypedModel.Note,
    val originalNote: ViewTypedModel.Note,
    val error: DetailsScreenError? = null
) : Parcelable

@Parcelize
sealed class DetailsScreenError(
    val message: String? = null
) : Parcelable {
    sealed interface Title {
        class Empty : DetailsScreenError(), Title
        class Large : DetailsScreenError(), Title
    }
    data class Other(val msg: String) : DetailsScreenError(msg)
}