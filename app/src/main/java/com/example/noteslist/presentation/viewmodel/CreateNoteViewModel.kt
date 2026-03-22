package com.example.noteslist.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.noteslist.domain.usecase.CreateNoteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CreateNoteViewModel(
    private val createNoteUseCase: CreateNoteUseCase = CreateNoteUseCase()
) : ViewModel() {
    private val _titleInput = MutableStateFlow("")
    val titleInput = _titleInput.asStateFlow()
    private val _descriptionInput = MutableStateFlow("")
    val descriptionInput = _descriptionInput.asStateFlow()
    private val _isImportantInput = MutableStateFlow(false)
    val isImportant = _isImportantInput.asStateFlow()

    fun updateTitle(value: String) {
        _titleInput.update { value }
    }

    fun updateDescription(value: String) {
        _descriptionInput.update { value }
    }
    fun updateIsImportant(value: Boolean) {
        _isImportantInput.update { value }
    }

    fun createNote() {
        createNoteUseCase.invoke(
            _titleInput.value,
            _descriptionInput.value,
            _isImportantInput.value
        )
    }

    fun cancel() {

    }
}