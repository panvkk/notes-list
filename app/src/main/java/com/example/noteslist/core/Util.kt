package com.example.noteslist.core

import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun String.toLocalDate() : LocalDate? {
    val pattern = "dd.MM.yyyy"
    val formatter = DateTimeFormatter.ofPattern(pattern)

    val localDate: LocalDate? = LocalDate.parse(this, formatter)

    return localDate
}