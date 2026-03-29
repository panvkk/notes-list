package com.example.noteslist.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.noteslist.domain.usecase.CreateNoteUseCase
import com.example.noteslist.domain.usecase.FindNoteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import com.example.noteslist.NotesListApplication

class NoteDetailsViewModel(
    private val createNoteUseCase: CreateNoteUseCase,
    private val findNoteUseCase: FindNoteUseCase
) : ViewModel() {

    private val noteId = MutableStateFlow<Long?>(null)

    private val _noteTitle = MutableStateFlow("")
    val noteTitle = _noteTitle.asStateFlow()
    private val _noteDescription = MutableStateFlow("")
    val noteDescription = _noteDescription.asStateFlow()
    private val _isNoteImportant = MutableStateFlow(false)
    val isNoteImportant = _isNoteImportant.asStateFlow()

    fun updateNoteTitle(value: String) {
        _noteTitle.update { value }
    }

    fun updateNoteDescription(value: String) {
        _noteDescription.update { value }
    }
    fun updateIsNoteImportant(value: Boolean) {
        _isNoteImportant.update { value }
    }

    fun setNewNote(newNoteId: Long?) {
        if(newNoteId == null) {
            noteId.value = null
            updateNoteTitle("")
            updateNoteDescription("")
            updateIsNoteImportant(false)
        } else {
            val newNote = findNoteUseCase.invoke(newNoteId) ?: return
            noteId.value = newNote.id
            updateNoteTitle(newNote.title)
            updateNoteDescription(newNote.description)
            updateIsNoteImportant(newNote.isImportant)
        }
    }

    fun createNote() {
        createNoteUseCase.invoke(
            _noteTitle.value,
            _noteDescription.value,
            _isNoteImportant.value
        )
    }

    fun cancel() {

    }

    companion object {
        val factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as NotesListApplication
                NoteDetailsViewModel(
                    application.createNoteUseCase,
                    application.findNoteUseCase
                )
            }
        }
    }
}