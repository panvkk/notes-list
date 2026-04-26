package com.example.noteslist.domain.usecase

import com.example.noteslist.core.domain.error.NoteValidationException
import com.example.noteslist.domain.model.NoteModel
import com.example.noteslist.domain.repository.NotesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class UpdateNoteUseCase(
    private val repository: NotesRepository,
    private val applicationScope: CoroutineScope
) {
    operator fun invoke(note: NoteModel?) : Result<Unit> {
        if(note == null || note.id == -1L)
            return Result.failure(IllegalArgumentException("Note with null id cannot be saved."))
        else if(note.title.isEmpty())
            return Result.failure(NoteValidationException.TitleEmpty())

        applicationScope.launch { repository.updateNote(note) }
        return Result.success(Unit)
    }
}