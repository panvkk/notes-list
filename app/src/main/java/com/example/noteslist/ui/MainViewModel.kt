package com.example.noteslist.ui

import android.util.Log
import com.example.noteslist.core.toLocalDate
import com.example.noteslist.data.NotesRepository
import com.example.noteslist.data.ViewTyped

class MainViewModel(
    private val notesRepository: NotesRepository = NotesRepository()
) {
    companion object {
        private const val TAG = "MainViewModel"
    }

    fun getViewTypedData() : List<ViewTyped> {
        val notes = sortByDate(fetchNotes())

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

    private fun sortByDate(notes: List<ViewTyped.Note>) : List<ViewTyped.Note> {
        var sortedNotes: List<ViewTyped.Note> = emptyList()
        try {
             sortedNotes = notes.sortedWith { note1, note2 ->
                val date1 = note1.date.toLocalDate()
                val date2 = note2.date.toLocalDate()
                 if (date1 == null || date2 == null) throw Throwable("Error while parse: Date can not be null")

                 date1.compareTo(date2)
            }
        } catch (e: Throwable) {
            Log.e(TAG, e.message ?: "Unknown Error.")
        }
        return sortedNotes
    }

    private fun fetchNotes() : List<ViewTyped.Note> {
        return notesRepository.getNotes()
    }
}