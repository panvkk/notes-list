package com.example.noteslist.domain.repository

import com.example.noteslist.domain.model.NoteModel

interface NotesRepository {
    fun getNotes() : List<NoteModel>
}