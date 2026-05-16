package com.example.noteslist.domain.usecase

import com.example.noteslist.core.Resource

class UpdateNoteReadUseCase(
    private val findNoteUseCase: FindNoteUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase
) {
    suspend operator fun invoke(noteId: Long) {
        when(val result = findNoteUseCase.invoke(noteId) ) {
            is Resource.Success -> {
                val isNoteRead = result.data.isRead
                updateNoteUseCase.invoke(result.data.copy(isRead = !isNoteRead))
            }
            else -> {  }
        }
    }
}