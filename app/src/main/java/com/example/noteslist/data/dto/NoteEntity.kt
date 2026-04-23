package com.example.noteslist.data.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes_table")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val title: String,
    val description: String,
    @ColumnInfo(name = "is_important")
    val isImportant: Boolean,
    val date: String,
    @ColumnInfo(name = "is_read")
    val isRead: Boolean = false
)
