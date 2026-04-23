package com.example.noteslist.domain.usecase

import com.example.noteslist.domain.model.NoteModel
import com.example.noteslist.domain.repository.NotesRepository

class FindNoteUseCase(
    private val repository: NotesRepository
) {
    suspend operator fun invoke(id: Long) : Result<NoteModel> {
        return repository.findNote(id)
    }
}