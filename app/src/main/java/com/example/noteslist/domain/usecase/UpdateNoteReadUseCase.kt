package com.example.noteslist.domain.usecase

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class UpdateNoteReadUseCase(
    private val findNoteUseCase: FindNoteUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase,
    private val applicationScope: CoroutineScope
) {
    operator fun invoke(noteId: Long) {
        applicationScope.launch {
            findNoteUseCase.invoke(noteId).onSuccess {
                val isNoteRead = it.isRead
                updateNoteUseCase.invoke(it.copy(isRead = !isNoteRead))
            }
        }
    }
}