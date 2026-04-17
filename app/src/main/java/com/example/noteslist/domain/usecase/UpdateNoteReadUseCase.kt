package com.example.noteslist.domain.usecase

class UpdateNoteReadUseCase(
    private val findNoteUseCase: FindNoteUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase
) {
    operator fun invoke(noteId: Long) : Result<Unit> {
        findNoteUseCase.invoke(noteId).onSuccess {
            val isNoteRead = it.isRead
            updateNoteUseCase.invoke(it.copy(isRead = !isNoteRead))
        }.onFailure {
            return Result.failure(IllegalAccessException(it.message))
        }
        return Result.success(Unit)
    }
}