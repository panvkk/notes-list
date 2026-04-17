package com.example.noteslist.domain.usecase

import com.example.noteslist.core.domain.error.NoteValidationException
import com.example.noteslist.domain.repository.NotesRepository
import java.time.LocalDate

class CreateNoteUseCase(
    private val repository: NotesRepository
) {
    operator fun invoke(title: String, description: String, isImportant: Boolean) : Result<Unit> {
        val date = LocalDate.now()
        return if(title.isEmpty())
            Result.failure(NoteValidationException.TitleEmpty())
        else Result.success(repository.addNote(title, description, date, isImportant))
    }
}