package com.example.noteslist.core.domain.error

sealed class NoteValidationException(message: String) : Throwable(message) {
    object TitleEmpty : NoteValidationException("Необходимо заполнить")
}