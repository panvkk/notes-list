package com.example.noteslist.domain.usecase

import com.example.noteslist.core.Resource
import com.example.noteslist.core.domain.error.DomainError
import com.example.noteslist.domain.model.NoteModel
import com.example.noteslist.domain.repository.NotesRepository

class UpdateNoteUseCase(private val repository: NotesRepository) {
    suspend operator fun invoke(note: NoteModel?) : Resource<Unit> {
        if(note == null || note.id == -1L)
            return Resource.Error(DomainError.InvalidArgument.Note)
        else if(note.title.isEmpty())
            return Resource.Error(DomainError.ValidationError.NoteTitleEmpty)

        repository.updateNote(note)
        return Resource.Success(Unit)
    }
}