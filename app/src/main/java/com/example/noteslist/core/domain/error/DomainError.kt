package com.example.noteslist.core.domain.error

sealed interface DomainError {
    sealed interface InvalidArgument : DomainError {
        data object Note : InvalidArgument
    }
    sealed interface ValidationError : DomainError {
        data object NoteTitleEmpty : ValidationError
    }
    sealed interface StorageError : DomainError {
        data object NoDiskSpace : StorageError
        data class Other(val msg: String?) : StorageError
    }
    data class UnexpectedException(val e: Throwable) : DomainError
}