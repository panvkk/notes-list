package com.example.noteslist.presentation.viewmodel

import android.os.Bundle
import androidx.core.os.BundleCompat
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.noteslist.NotesListApplication
import com.example.noteslist.domain.usecase.CreateNoteUseCase
import com.example.noteslist.domain.usecase.FindNoteUseCase
import com.example.noteslist.domain.usecase.UpdateNoteUseCase
import com.example.noteslist.presentation.mappers.toDomain
import com.example.noteslist.presentation.mappers.toUiModel
import com.example.noteslist.presentation.model.DetailsUiState
import com.example.noteslist.presentation.model.ViewTypedModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NoteDetailsViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val createNoteUseCase: CreateNoteUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase,
    private val findNoteUseCase: FindNoteUseCase
) : ViewModel() {

    init {
        savedStateHandle.setSavedStateProvider(STATE_BUNDLE_KEY) {
            Bundle().apply {
                putParcelable(STATE_KEY, _uiState.value)
            }
        }
    }

    private val _uiState = MutableStateFlow(generateInitialState())
    val uiState = _uiState.asStateFlow()

    private val _isNewNote = MutableStateFlow(true)
    val isNewNote = _isNewNote.asStateFlow()

    fun updateNoteTitle(value: String) {
        _uiState.update { it.copy(note = it.note.copy(title = value)) }
    }
    fun updateNoteDescription(value: String) {
        _uiState.update { it.copy(note = it.note.copy(description = value)) }
    }
    fun updateIsNoteImportant(value: Boolean) {
        _uiState.update { it.copy(note = it.note.copy(isImportant = value)) }
    }
    fun updateIsNoteRead(value: Boolean) {
        _uiState.update { it.copy(note = it.note.copy(isRead = value)) }
    }
    private fun setNote(value: ViewTypedModel.Note) {
        _uiState.update { it.copy(note = value) }
    }
    private fun setNoteId(value: Long?) {
        _uiState.update { it.copy(noteId = value) }
    }
    private fun updateIsNewNote(value: Boolean) {
        _isNewNote.value = value
    }

    fun setCurrentNote(newNoteId: Long?) {
        if(newNoteId == null) {
            updateIsNewNote(true)
            setNoteId(null)
            updateNoteTitle("")
            updateNoteDescription("")
            updateIsNoteImportant(false)
        } else {
            updateIsNewNote(false)
            val newNote = findNoteUseCase.invoke(newNoteId)?.toUiModel() ?: return
            setNoteId(newNote.id)
            setNote(newNote)
        }
    }

    fun createNote() {
        _uiState.value.note.apply {
            createNoteUseCase.invoke(title, description, isImportant)
        }
    }

    fun updateNote() {
        if(_uiState.value.noteId == null) throw IllegalStateException("Note with null id cannot be saved.")

        updateNoteUseCase.invoke(_uiState.value.note.toDomain())
    }

    private fun generateInitialState() : DetailsUiState {
        val savedState = savedStateHandle.get<Bundle>(STATE_BUNDLE_KEY)?.let {
            BundleCompat.getParcelable(it, STATE_KEY, DetailsUiState::class.java)
        }

        return savedState ?: DetailsUiState(
            noteId = null,
            ViewTypedModel.Note(-1, "", "", false, "", false)
        )
    }

    companion object {
        private const val TAG = "NoteDetailsViewModel"
        private const val STATE_BUNDLE_KEY = "state_bundle"
        private const val STATE_KEY = "state"
        val factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as NotesListApplication
                NoteDetailsViewModel(
                    createSavedStateHandle(),
                    application.createNoteUseCase,
                    application.updateNoteUseCase,
                    application.findNoteUseCase
                )
            }
        }
    }
}