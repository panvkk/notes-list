package com.example.noteslist.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DetailsUiState(
    val noteId: Long?,
    val note: ViewTypedModel.Note,
    val error: DetailsScreenError? = null
) : Parcelable

@Parcelize
sealed class DetailsScreenError(
    val message: String
) : Parcelable {
    data class TitleEmpty(val msg: String) : DetailsScreenError(msg)
    data class Other(val msg: String) : DetailsScreenError(msg)
}