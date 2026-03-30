package com.example.noteslist.presentation.mappers

import com.example.noteslist.core.presentation.toLocalDate
import com.example.noteslist.core.presentation.toStringWithPattern
import com.example.noteslist.domain.model.NoteModel
import com.example.noteslist.presentation.model.ViewTypedModel

fun NoteModel.toUiModel() = ViewTypedModel.Note(
    id = id,
    title = title,
    description = description,
    date = date?.toStringWithPattern()
        ?: throw IllegalStateException("Error while parse: Date cannot be null."),
    isImportant = isImportant,
    isRead = isRead
)

fun ViewTypedModel.Note.toDomain() = NoteModel(
    id = id,
    title = title,
    description = description,
    date = date.toLocalDate(),
    isImportant = isImportant,
    isRead = isRead
)