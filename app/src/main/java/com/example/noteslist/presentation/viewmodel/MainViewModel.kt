package com.example.noteslist.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.noteslist.NotesListApplication
import com.example.noteslist.domain.usecase.NotesUseCase
import com.example.noteslist.presentation.mappers.toUiModel
import com.example.noteslist.presentation.model.ViewTyped

class MainViewModel(
    private val notesUseCase: NotesUseCase
) : ViewModel() {
    companion object {
        private const val TAG = "MainViewModel"

        val factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as NotesListApplication
                MainViewModel(application.notesUseCase)
            }
        }
    }

    fun getViewTypedData() : List<ViewTyped> {
        val notes = getNotes()

        val viewTypedData = mutableListOf<ViewTyped>()
        val notImportantNotes = mutableListOf<ViewTyped.Note>()
        var currentDate = notes.first().date
        // Самая первая ближайшая дата
        viewTypedData.add(ViewTyped.DateTitle(currentDate))
        notes.forEach { note ->
            // Добавляем вьютайп разделителя даты
            if(currentDate != note.date) {
                if(notImportantNotes.size > 1) {                          // Если осталось больше 1
                    val noteStackChildren = notImportantNotes.toList()
                    val noteStack = ViewTyped.NoteStack(noteStackChildren)
                    viewTypedData.add(noteStack)
                } else if (notImportantNotes.isNotEmpty())                 // Если остался 1
                    viewTypedData.add(notImportantNotes.first())
                notImportantNotes.clear()

                currentDate = note.date
                viewTypedData.add(ViewTyped.DateTitle(currentDate))
            }
            // Разбиваем на вьютайпы для NoteStackView и NoteView
            if(note.isImportant) {
                if(notImportantNotes.size > 1) {
                    val noteStackChildren = notImportantNotes.toList()
                    val noteStack = ViewTyped.NoteStack(noteStackChildren)
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
            val noteStack = ViewTyped.NoteStack(noteStackChildren)
            viewTypedData.add(noteStack)
        } else if (notImportantNotes.isNotEmpty())                 // Если остался 1
            viewTypedData.add(notImportantNotes.first())

        return viewTypedData
    }

    private fun getNotes() : List<ViewTyped.Note> {
        return try {
            notesUseCase.invoke().map { noteModel -> noteModel.toUiModel() }
        } catch (e: Throwable) {
            Log.e(TAG, e.message ?: "Unknown Error")
            emptyList()
        }
    }
}