package com.example.noteslist.domain.usecase

import android.util.Log
import com.example.noteslist.data.local.NotesRepositoryImpl
import com.example.noteslist.domain.model.NoteModel
import com.example.noteslist.domain.repository.NotesRepository

class NotesUseCase(
    // Когда появится DI, тут будет лучше, а пока нарушается независимость domain слоя
    private val repository: NotesRepository = NotesRepositoryImpl()
) {
    companion object {
        private const val TAG = "NotesUseCase"
    }

    operator fun invoke() : List<NoteModel> {
        val notes = repository.getNotes()
        return sortByDate(notes)
    }

    private fun sortByDate(notes: List<NoteModel>) : List<NoteModel> {
        var sortedNotes: List<NoteModel> = emptyList()
        try {
            sortedNotes = notes.sortedWith { note1, note2 ->
                val date1 = note1.date
                val date2 = note2.date
                if (date1 == null || date2 == null)
                    throw IllegalArgumentException("Error while parse: Date can not be null")

                date1.compareTo(date2)
            }
        } catch (e: Throwable) {
            Log.e(TAG, e.message ?: "Unknown Error.")
        }
        return sortedNotes
    }
}