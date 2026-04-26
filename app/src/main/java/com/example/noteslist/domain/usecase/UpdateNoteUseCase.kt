package com.example.noteslist.domain.usecase

import com.example.noteslist.core.domain.error.DomainError
import com.example.noteslist.domain.model.NoteModel
import com.example.noteslist.domain.repository.NotesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext

class UpdateNoteUseCase(
    private val repository: NotesRepository,
    private val applicationScope: CoroutineScope
) {
    suspend operator fun invoke(note: NoteModel?) : Result<Unit> = withContext(applicationScope.coroutineContext) {
        if(note == null || note.id == -1L)
            return@withContext Result.failure(DomainError.InvalidArgument.Note())
        else if(note.title.isEmpty())
            return@withContext Result.failure(DomainError.Validation.NoteTitleEmpty())

        repository.updateNote(note)
    }
}