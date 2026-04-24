package com.example.noteslist.presentation.model

sealed interface NotesListUiState {
    data object Loading : NotesListUiState
    data class Content(val notesViewTypedModel: List<ViewTypedModel>) : NotesListUiState
}