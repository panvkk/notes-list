package com.example.noteslist.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.noteslist.NotesListApplication
import com.example.noteslist.core.MAX_TITLE_LENGTH
import com.example.noteslist.core.domain.error.NoteValidationException
import com.example.noteslist.domain.usecase.CreateNoteUseCase
import com.example.noteslist.domain.usecase.FindNoteUseCase
import com.example.noteslist.domain.usecase.UpdateNoteUseCase
import com.example.noteslist.presentation.mappers.toDomain
import com.example.noteslist.presentation.mappers.toUiModel
import com.example.noteslist.presentation.model.DetailsScreenError
import com.example.noteslist.presentation.model.DetailsUiState
import com.example.noteslist.presentation.model.ViewTypedModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NoteDetailsViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val createNoteUseCase: CreateNoteUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase,
    private val findNoteUseCase: FindNoteUseCase
) : ViewModel() {

    val uiState = savedStateHandle.getStateFlow(STATE_KEY, generateInitialState())

    private val _isNewNote = MutableStateFlow(true)
    val isNewNote = _isNewNote.asStateFlow()

    private val _navigationEvent = Channel<NavigationEvent>()
    val navigationEvent = _navigationEvent.receiveAsFlow()

    val hasUnsavedChanges = uiState.map { state ->
        state.originalNote != state.currentNote
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), false)


    init {
        // Сделал одну подписку на стейт, чтобы не создавать каждый раз новую корутину в updateNoteTitle()
        // вдруг пользователь печатает слишком быстро
        viewModelScope.launch(Dispatchers.Default) {
            uiState.map { it.currentNote.title }
                .distinctUntilChanged()
                .collect { title ->
                    val error = if(title.length > MAX_TITLE_LENGTH)
                        DetailsScreenError.HasTitle.Large()
                    else null // сброс всех ошибок title при вводе символа

                    if(uiState.value.error != error)
                        updateError(error)
                }
        }
    }
    fun updateNoteTitle(value: String) {
        savedStateHandle.update(STATE_KEY, { generateInitialState() }) {
            it.copy(currentNote = it.currentNote.copy(title = value))
        }
    }
    fun updateNoteDescription(value: String) {
        savedStateHandle.update(STATE_KEY, { generateInitialState() }) {
            it.copy(currentNote = it.currentNote.copy(description = value))
        }
    }
    fun updateIsNoteImportant(value: Boolean) {
        savedStateHandle.update(STATE_KEY, { generateInitialState() }) {
            it.copy(currentNote = it.currentNote.copy(isImportant = value))
        }
    }
    fun updateIsNoteRead(value: Boolean) {
        savedStateHandle.update(STATE_KEY, { generateInitialState() }) {
            it.copy(currentNote = it.currentNote.copy(isRead = value))
        }
    }
    private fun setCurrentAndOriginalNotes(value: ViewTypedModel.Note) {
        savedStateHandle.update(STATE_KEY, { generateInitialState() }) {
            it.copy(currentNote = value, originalNote = value)
        }
    }
    private fun setNoteId(value: Long?) {
        savedStateHandle.update(STATE_KEY, { generateInitialState() }) {
            it.copy(noteId = value)
        }
    }
    private fun updateIsNewNote(value: Boolean) {
        _isNewNote.value = value
    }
    private fun updateError(error: DetailsScreenError?) {
        savedStateHandle.update(STATE_KEY, { generateInitialState() }) {
            it.copy(error = error)
        }
    }

    fun setNote(newNoteId: Long?) {
        viewModelScope.launch {
            updateError(null)
            if (newNoteId == null) {
                updateIsNewNote(true)
                setNoteId(null)
                setCurrentAndOriginalNotes(ViewTypedModel.Note(-1, "", "", false, "", false))
            } else {
                var newNote: ViewTypedModel.Note? = null
                findNoteUseCase.invoke(newNoteId)
                    .onSuccess { newNote = it.toUiModel() }
                    .onFailure {
                        val msg = it.message ?: "Unknown error."
                        updateError(DetailsScreenError.Other(msg))
                        Log.e(TAG, msg)
                    }
                setNoteId(newNote?.id ?: return@launch)
                setCurrentAndOriginalNotes(newNote)
                updateIsNewNote(false)
            }
        }
    }

    fun submitNote() {
        viewModelScope.launch {
            uiState.value.currentNote.apply {
                if (uiState.value.error is DetailsScreenError.HasTitle) return@launch

                val result = if (_isNewNote.value) {
                    createNoteUseCase.invoke(title, description, isImportant)
                } else {
                    updateNoteUseCase.invoke(uiState.value.currentNote.toDomain())
                }
                result
                    .onSuccess {
                        viewModelScope.launch {
                            _navigationEvent.send(NavigationEvent.OnSave)
                        }
                    }.onFailure { exception ->
                        val msg = exception.message ?: "Unknown error."
                        when (exception) {
                            is NoteValidationException.TitleEmpty -> {
                                updateError(DetailsScreenError.HasTitle.Empty())
                            }

                            else -> {
                                updateError(DetailsScreenError.Other(msg))
                                Log.e(TAG, msg)
                            }
                        }
                    }
            }
        }
    }

    fun cancel() {
        viewModelScope.launch {
            _navigationEvent.send(NavigationEvent.OnCancel)
        }
    }
    private fun generateInitialState() : DetailsUiState {
        return DetailsUiState(
            noteId = null,
            ViewTypedModel.Note(-1, "", "", false, "", false),
            ViewTypedModel.Note(-1, "", "", false, "", false)
        )
    }

    companion object {
        private const val TAG = "NoteDetailsViewModel"
        private const val STATE_KEY = "details_state"
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

// Экстеншн, упрощающий обновление стэйта, так как он маппится из SavedStateHandle
inline fun <T> SavedStateHandle.update(key: String, generateInitialState: () -> T, block: (T) -> T) {
    val currentState = this.get<T>(key) ?: generateInitialState()
    this[key] = block(currentState)
}

sealed interface NavigationEvent {
    data object OnCancel : NavigationEvent
    data object OnSave : NavigationEvent
}
