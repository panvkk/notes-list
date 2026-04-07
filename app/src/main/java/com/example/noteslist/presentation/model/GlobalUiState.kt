package com.example.noteslist.presentation.model

sealed interface GlobalUiState {
    data class EditNote(val noteId: Long) : GlobalUiState
    data object CreateNote : GlobalUiState
    data object Idle: GlobalUiState
}