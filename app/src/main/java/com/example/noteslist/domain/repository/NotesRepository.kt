package com.example.noteslist.domain.repository

import com.example.noteslist.domain.model.NoteModel
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface NotesRepository {
    fun getNotes() : Flow<List<NoteModel>>

    fun getNotesByQuery(query: String) : Flow<List<NoteModel>>

    suspend fun createNote(title: String, description: String, date: LocalDate, isImportant: Boolean): Result<Unit>

    suspend fun updateNote(note: NoteModel) : Result<Unit>

    suspend fun findNote(id: Long) : Result<NoteModel>
}