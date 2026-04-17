package com.example.noteslist.core.domain.error

sealed class NoteValidationException : Throwable() {
    class TitleEmpty : NoteValidationException()
}