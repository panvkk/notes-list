package com.example.noteslist.domain.usecase

import com.example.noteslist.core.domain.error.DomainError
import com.example.noteslist.core.domain.error.NoteValidationException
import com.example.noteslist.domain.repository.NotesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

class CreateNoteUseCase(
    private val repository: NotesRepository,
    private val applicationScope: CoroutineScope
) {
    suspend operator fun invoke(
        title: String,
        description: String,
        isImportant: Boolean
    ): Result<Unit> = withContext(applicationScope.coroutineContext) {
        val date = LocalDate.now()
        if(title.isEmpty())
            return@withContext Result.failure(DomainError.Validation.NoteTitleEmpty())

        repository.createNote(title, description, date, isImportant)
    }
}