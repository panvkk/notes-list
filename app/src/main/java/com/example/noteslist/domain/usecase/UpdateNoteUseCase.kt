package com.example.noteslist.domain.usecase

import com.example.noteslist.core.domain.error.NoteValidationException
import com.example.noteslist.domain.model.NoteModel
import com.example.noteslist.domain.repository.NotesRepository

class UpdateNoteUseCase(
    private val repository: NotesRepository
) {
    suspend operator fun invoke(note: NoteModel?) : Result<Unit> {
        return if(note == null || note.id == -1L)
            Result.failure(IllegalArgumentException("Note with null id cannot be saved."))
        else if(note.title.isEmpty())
            Result.failure(NoteValidationException.TitleEmpty())
        else repository.updateNote(note)
    }
}