package com.example.noteslist.core

import com.example.noteslist.core.domain.error.DomainError
import java.time.LocalDate
import java.time.format.DateTimeFormatter

sealed interface Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>
    data class Error(val error: DomainError) : Resource<Nothing>
}

fun String.toLocalDate() : LocalDate? {
    val pattern = "dd.MM.yyyy"
    val formatter = DateTimeFormatter.ofPattern(pattern)

    val localDate: LocalDate? = LocalDate.parse(this, formatter)

    return localDate
}

fun LocalDate.toStringWithPattern() : String {
    var result: String
    val pattern = "dd.MM.yyyy"
    val formatter = DateTimeFormatter.ofPattern(pattern)

    result = this.format(formatter)

    return result
}