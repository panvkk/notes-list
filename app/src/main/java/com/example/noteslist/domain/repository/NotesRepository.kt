package com.example.noteslist.domain.repository

import com.example.noteslist.domain.model.NoteModel
import java.time.LocalDate

interface NotesRepository {
    fun getNotes() : List<NoteModel>

    fun addNote(title: String, description: String, date: LocalDate, isImportant: Boolean)

    fun updateNote(note: NoteModel) : Boolean
}