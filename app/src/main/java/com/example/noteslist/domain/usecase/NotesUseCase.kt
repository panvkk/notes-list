package com.example.noteslist.domain.usecase

import android.util.Log
import com.example.noteslist.domain.model.NoteModel
import com.example.noteslist.domain.repository.NotesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList

class NotesUseCase(
    private val repository: NotesRepository
) {
    companion object {
        private const val TAG = "NotesUseCase"
    }

    operator fun invoke() : Flow<List<NoteModel>> {
        return repository.getNotes().map { notes ->
            try {
                sortByDate(notes)
            } catch (e: Throwable) {
                Log.e(TAG, e.message ?: "Unknown Error")
                emptyList()
            }
        }
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