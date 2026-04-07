package com.example.noteslist.presentation.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GlobalViewModel : ViewModel() {
    private val _selectedNoteId = MutableStateFlow<Long?>(null)
    val selectedNoteId = _selectedNoteId.asStateFlow()

    var hasUnsavedChanges: Boolean = false

    fun selectNote(noteId: Long?) {
        _selectedNoteId.update { noteId }
    }
}
