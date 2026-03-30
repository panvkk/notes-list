package com.example.noteslist.domain.usecase

import com.example.noteslist.domain.model.NoteModel
import com.example.noteslist.domain.repository.NotesRepository

class UpdateNoteUseCase(
    private val repository: NotesRepository
) {
    operator fun invoke(note: NoteModel) : Boolean {
        return repository.updateNote(note)
    }
}