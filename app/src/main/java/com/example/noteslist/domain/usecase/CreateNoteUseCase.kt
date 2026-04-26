package com.example.noteslist.domain.usecase

import com.example.noteslist.core.domain.error.NoteValidationException
import com.example.noteslist.domain.repository.NotesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDate

class CreateNoteUseCase(
    private val repository: NotesRepository,
    private val applicationScope: CoroutineScope
) {
    operator fun invoke(title: String, description: String, isImportant: Boolean) : Result<Unit> {
        val date = LocalDate.now()
        if(title.isEmpty())
            return Result.failure(NoteValidationException.TitleEmpty())

        applicationScope.launch {
            repository.createNote(title, description, date, isImportant)
        }
        return Result.success(Unit)
    }
}