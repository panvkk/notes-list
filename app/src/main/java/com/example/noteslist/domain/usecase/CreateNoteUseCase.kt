package com.example.noteslist.domain.usecase

import com.example.noteslist.data.local.NotesRepositoryImpl
import com.example.noteslist.domain.repository.NotesRepository
import java.time.LocalDate

class CreateNoteUseCase(
    private val repository: NotesRepository = NotesRepositoryImpl()
) {
    operator fun invoke(title: String, description: String, isImportant: Boolean) {
        val date = LocalDate.now()
        repository.addNote(title, description, date, isImportant)
    }
}