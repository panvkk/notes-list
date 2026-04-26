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
    sealed interface HasTitle {
        @Parcelize class Empty : DetailsScreenError(), HasTitle
        @Parcelize class Large : DetailsScreenError(), HasTitle
    }
    @Parcelize class NoDiscSpace : DetailsScreenError()
    @Parcelize data class Other(val msg: String?) : DetailsScreenError(msg)
}