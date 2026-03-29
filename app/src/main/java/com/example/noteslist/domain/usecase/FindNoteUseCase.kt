package com.example.noteslist.domain.usecase

import android.util.Log
import com.example.noteslist.data.local.NotesRepositoryImpl
import com.example.noteslist.domain.model.NoteModel
import com.example.noteslist.domain.repository.NotesRepository
import java.time.LocalDate

class FindNoteUseCase(
    private val repository: NotesRepository
) {
    companion object {
        const val TAG = "FindNoteUseCase"
    }
    operator fun invoke(id: Long) : NoteModel? {
        return try {
            repository.findNoteById(id)
        } catch (e: Exception) {
            Log.e(TAG, e.message ?: "Unknown message")
            null
        }
    }
}