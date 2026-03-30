package com.example.noteslist.presentation.viewmodel

import android.os.Bundle
import androidx.core.os.BundleCompat
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.noteslist.domain.usecase.CreateNoteUseCase
import com.example.noteslist.domain.usecase.FindNoteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import com.example.noteslist.NotesListApplication
import com.example.noteslist.presentation.model.DetailsUiState

class NoteDetailsViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val createNoteUseCase: CreateNoteUseCase,
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

    fun updateNoteTitle(value: String) {
        _uiState.update { it.copy(noteTitle = value) }
    }

    fun updateNoteDescription(value: String) {
        _uiState.update { it.copy(noteDescription = value) }
    }
    fun updateIsNoteImportant(value: Boolean) {
        _uiState.update { it.copy(isNoteImportant = value) }
    }

    fun updateNoteId(value: Long?) {
        _uiState.update { it.copy(noteId = value) }
    }

    fun setCurrentNote(newNoteId: Long?) {
        if(newNoteId == null) {
            updateNoteId(null)
            updateNoteTitle("")
            updateNoteDescription("")
            updateIsNoteImportant(false)
        } else {
            val newNote = findNoteUseCase.invoke(newNoteId) ?: return
            updateNoteId(newNote.id)
            updateNoteTitle(newNote.title)
            updateNoteDescription(newNote.description)
            updateIsNoteImportant(newNote.isImportant)
        }
    }

    fun createNote() {
        _uiState.value.apply {
            createNoteUseCase.invoke(
                noteTitle,
                noteDescription,
                isNoteImportant
            )
        }
    }

    fun cancel() {

    }

    private fun generateInitialState() : DetailsUiState {
        val savedState = savedStateHandle.get<Bundle>(STATE_BUNDLE_KEY)?.let {
            BundleCompat.getParcelable(it, STATE_KEY, DetailsUiState::class.java)
        }

        return savedState ?: DetailsUiState(
            noteId = null,
            noteTitle = "", noteDescription = "", isNoteImportant = false)
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
                    application.findNoteUseCase
                )
            }
        }
    }
}