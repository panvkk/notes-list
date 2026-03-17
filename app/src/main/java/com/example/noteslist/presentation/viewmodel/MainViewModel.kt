package com.example.noteslist.presentation.viewmodel

import android.util.Log
import com.example.noteslist.core.presentation.toLocalDate
import com.example.noteslist.core.presentation.toStringWithPattern
import com.example.noteslist.data.local.NotesRepositoryImpl
import com.example.noteslist.domain.model.NoteModel
import com.example.noteslist.presentation.model.ViewTyped
import com.example.noteslist.domain.repository.NotesRepository
import com.example.noteslist.domain.usecase.NotesUseCase
import kotlin.collections.forEach

class MainViewModel(
    private val notesUseCase: NotesUseCase = NotesUseCase()
) {
    companion object {
        private const val TAG = "MainViewModel"
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

    private fun NoteModel.toUiModel() = ViewTyped.Note(
        title = title,
        description = description,
        date = date?.toStringWithPattern()
            ?: throw IllegalStateException("Error while parse: Date cannot be null."),
        isImportant = isImportant,
        isRead = isRead
    )
}