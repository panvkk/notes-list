package com.example.noteslist.core.domain.error

sealed class DomainError(msg: String? = null) : Throwable(msg) {
    sealed class InvalidArgument : DomainError() {
        class Note : InvalidArgument()
    }
    sealed class Validation : DomainError() {
        class NoteTitleEmpty : Validation()
    }
    sealed class Data(msg: String? = null) : DomainError(msg) {
        class NoDiskSpace : Data()
        class Other(msg: String?) : Data(msg)
    }
}