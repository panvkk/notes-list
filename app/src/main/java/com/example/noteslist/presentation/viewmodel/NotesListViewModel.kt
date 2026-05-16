package com.example.noteslist.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.noteslist.NotesListApplication
import com.example.noteslist.domain.model.AppConfig
import com.example.noteslist.domain.model.SettingsModel
import com.example.noteslist.domain.usecase.GetAppConfigUseCase
import com.example.noteslist.domain.usecase.GetSettingsUseCase
import com.example.noteslist.domain.usecase.NotesUseCase
import com.example.noteslist.domain.usecase.UpdateAppConfigUseCase
import com.example.noteslist.domain.usecase.UpdateNoteReadUseCase
import com.example.noteslist.presentation.mappers.toUiModel
import com.example.noteslist.presentation.model.NotesListUiState
import com.example.noteslist.presentation.model.ViewTypedModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NotesListViewModel(
    private val notesUseCase: NotesUseCase,
    private val updateNoteReadUseCase: UpdateNoteReadUseCase,
    private val getSettingsUseCase: GetSettingsUseCase,
    private val getAppConfigUseCase: GetAppConfigUseCase,
    private val updateAppConfigUseCase: UpdateAppConfigUseCase
) : ViewModel() {

    private val _expandedStackIds = MutableStateFlow<Set<Int>>(emptySet())
    private val _searchQuery = MutableStateFlow("")

    private val debouncedSearchQuery = _searchQuery
        .debounce { if(it.isEmpty()) 0L else 500L }
        .distinctUntilChanged()

    val currentSettings = getSettingsUseCase.invokeFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), getDefaultSettings())

    val uiState: StateFlow<NotesListUiState> = combine(
        debouncedSearchQuery,
        getAppConfigUseCase.invokeFlow().map { it.isFirstEntry }
    ) { searchQuery, isFirstEntry ->
        object {
            val query = searchQuery
            val isFirstEntry = isFirstEntry
        }
    }.flatMapLatest { params ->
        if(params.isFirstEntry) {
            combine(
                loadNotes(params.query),
                flowOf(Unit).onEach { delay(500L) }
            ) { viewTyped, _ ->
                updateIsFirstEntry(false)
                NotesListUiState.Content(viewTyped) as NotesListUiState
            }.onStart { emit(NotesListUiState.Loading) }
        } else {
            loadNotes(params.query).map { NotesListUiState.Content(it) }
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        NotesListUiState.Content(emptyList())
    )

    fun onNoteLongClick(noteId: Long) {
        viewModelScope.launch {
            updateNoteReadUseCase.invoke(noteId)
                .onFailure { Log.e(TAG, it.message ?: "Unknown Error.")  }
        }
    }

    private fun getDefaultSettings() = SettingsModel(50f, 3)

    private fun getViewTypedData(notes: List<ViewTypedModel.Note>) : List<ViewTypedModel> {
        if(notes.isEmpty()) return emptyList()

        val viewTypedData = mutableListOf<ViewTypedModel>()
        val notImportantNotes = mutableListOf<ViewTypedModel.Note>()
        var currentDate = notes.first().date
        // Самая первая ближайшая дата
        viewTypedData.add(ViewTypedModel.DateTitle(currentDate))
        var nextStackId = 0
        notes.forEach { note ->
            // Добавляем вьютайп разделителя даты
            if(currentDate != note.date) {
                if(notImportantNotes.size > 1) {                          // Если осталось больше 1
                    val noteStackChildren = notImportantNotes.toList()
                    val noteStack = ViewTypedModel.NoteStack(
                        nextStackId, noteStackChildren, isStackExpanded(nextStackId++))
                    viewTypedData.add(noteStack)
                } else if (notImportantNotes.isNotEmpty())                 // Если остался 1
                    viewTypedData.add(notImportantNotes.first())
                notImportantNotes.clear()

                currentDate = note.date
                viewTypedData.add(ViewTypedModel.DateTitle(currentDate))
            }
            // Разбиваем на вьютайпы для NoteStackView и NoteView
            if(note.isImportant) {
                if(notImportantNotes.size > 1) {
                    val noteStackChildren = notImportantNotes.toList()
                    val noteStack = ViewTypedModel.NoteStack(
                        nextStackId, noteStackChildren, isStackExpanded(nextStackId++))
                    viewTypedData.add(noteStack)
                } else if(notImportantNotes.isNotEmpty()) {
                    viewTypedData.add(notImportantNotes.first())
                }
                notImportantNotes.clear()
                viewTypedData.add(note)
            } else {
                notImportantNotes.add(note)
            }
        }
        if(notImportantNotes.size > 1) {                          // Если осталось больше 1
            val noteStackChildren = notImportantNotes.toList()
            val noteStack = ViewTypedModel.NoteStack(
                nextStackId, noteStackChildren, isStackExpanded(nextStackId)
            )
            viewTypedData.add(noteStack)
        } else if (notImportantNotes.isNotEmpty())                 // Если остался 1
            viewTypedData.add(notImportantNotes.first())

        return viewTypedData
    }

    fun expandStack(stackId: Int) {
        _expandedStackIds.value += stackId
    }
    fun collapseStack(stackId: Int) {
        _expandedStackIds.value -= stackId
    }
    fun updateSearchQuery(value: String) {
        _searchQuery.update { value }
    }
    private fun loadNotes(query: String) : Flow<List<ViewTypedModel>> {
        return notesUseCase.invoke(query).map { noteModels ->
            val notes = noteModels.map { it.toUiModel() }

            getViewTypedData(notes)
        }
    }
    private suspend fun updateIsFirstEntry(value: Boolean) {
        updateAppConfigUseCase.invoke(AppConfig(value))
    }
    fun isStackExpanded(stackId: Int) = _expandedStackIds.value.contains(stackId)

    companion object {
        private const val TAG = "NotesListViewModel"
        val factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as NotesListApplication
                NotesListViewModel(
                    application.notesUseCase,
                    application.updateNoteReadUseCase,
                    application.getSettingsUseCase,
                    application.getAppConfigUseCase,
                    application.updateAppConfigUseCase
                )
            }
        }
    }
}