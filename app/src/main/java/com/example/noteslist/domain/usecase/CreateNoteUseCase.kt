package com.example.noteslist.domain.usecase

import com.example.noteslist.core.Resource
import com.example.noteslist.core.domain.error.DomainError
import com.example.noteslist.domain.repository.NotesRepository
import java.time.LocalDate

class CreateNoteUseCase(private val repository: NotesRepository) {
    suspend operator fun invoke(
        title: String,
        description: String,
        isImportant: Boolean
    ) : Resource<Unit> {
        val date = LocalDate.now()
        if(title.isEmpty())
            return Resource.Error(DomainError.ValidationError.NoteTitleEmpty)

        repository.createNote(title, description, date, isImportant)
        return Resource.Success(Unit)
    }
}