package com.example.noteslist.domain.repository

import com.example.noteslist.domain.model.NoteModel
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface NotesRepository {
    fun getNotes(query: String) : Flow<List<NoteModel>>

    fun addNote(title: String, description: String, date: LocalDate, isImportant: Boolean)

    fun updateNote(note: NoteModel) : Result<Unit>

    fun findNoteById(id: Long) : Result<NoteModel>
}