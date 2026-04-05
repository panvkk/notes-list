package com.example.noteslist.domain.usecase

import com.example.noteslist.domain.model.NoteModel
import com.example.noteslist.domain.repository.NotesRepository

class FindNoteUseCase(
    private val repository: NotesRepository
) {
    operator fun invoke(id: Long) : Result<NoteModel> {
        return try {
            repository.findNoteById(id)
        } catch (e: IllegalArgumentException) {
            Result.failure(e)
        } catch (e: IllegalAccessException) {
            Result.failure(e)
        }
    }
}