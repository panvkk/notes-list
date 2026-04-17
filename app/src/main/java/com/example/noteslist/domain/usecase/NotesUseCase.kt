package com.example.noteslist.domain.usecase

import com.example.noteslist.domain.model.NoteModel
import com.example.noteslist.domain.repository.NotesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class NotesUseCase(
    private val repository: NotesRepository
) {
    operator fun invoke() : Flow<List<NoteModel>> {
        return repository.getNotes().map { notes -> sortByDate(notes) }
    }

    private fun sortByDate(notes: List<NoteModel>) : List<NoteModel> {
        // Заметки, дата которых == null будут в конце списка
        val comparator = compareBy<NoteModel, LocalDate?>(nullsLast()) { it.date }
        val sortedNotes = notes.sortedWith(comparator)
        return sortedNotes
    }
}