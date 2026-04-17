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
import com.example.noteslist.domain.usecase.FindNoteUseCase
import com.example.noteslist.domain.usecase.NotesUseCase
import com.example.noteslist.domain.usecase.UpdateNoteReadUseCase
import com.example.noteslist.domain.usecase.UpdateNoteUseCase
import com.example.noteslist.presentation.mappers.toDomain
import com.example.noteslist.presentation.mappers.toUiModel
import com.example.noteslist.presentation.model.ViewTypedModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class NotesListViewModel(
    private val notesUseCase: NotesUseCase,
    private val updateNoteReadUseCase: UpdateNoteReadUseCase
) : ViewModel() {

    val viewTypedNotes = notesUseCase.invoke()
        .map { noteModels ->
            val notes = noteModels.map { it.toUiModel() }
            getViewTypedData(notes)
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )
    fun onNoteLongClick(noteId: Long) {
        updateNoteReadUseCase.invoke(noteId)
            .onFailure { Log.e(TAG, it.message ?: "Unknown Error.")  }
    }
    private fun getViewTypedData(notes: List<ViewTypedModel.Note>) : List<ViewTypedModel> {
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
                    val noteStack = ViewTypedModel.NoteStack(nextStackId++, noteStackChildren)
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
                    val noteStack = ViewTypedModel.NoteStack(nextStackId++, noteStackChildren)
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
            val noteStack = ViewTypedModel.NoteStack(nextStackId++, noteStackChildren)
            viewTypedData.add(noteStack)
        } else if (notImportantNotes.isNotEmpty())                 // Если остался 1
            viewTypedData.add(notImportantNotes.first())

        return viewTypedData
    }
    companion object {
        private const val TAG = "NotesListViewModel"
        val factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as NotesListApplication
                NotesListViewModel(
                    application.notesUseCase,
                    application.updateNoteReadUseCase)
            }
        }
    }
}