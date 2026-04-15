package com.example.noteslist.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.noteslist.presentation.model.GlobalUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GlobalViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<GlobalUiState>(GlobalUiState.Idle)
    val uiState = _uiState.asStateFlow()
    var hasUnsavedChanges: Boolean = false

    fun openEditNote(noteId: Long) {
        _uiState.update { GlobalUiState.EditNote(noteId) }
    }
    fun openCreateNote() {
        _uiState.update { GlobalUiState.CreateNote }
    }
    fun closeDetails() {
        _uiState.update { GlobalUiState.Idle }
    }
}