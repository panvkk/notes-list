package com.example.noteslist.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.noteslist.data.dto.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDao {
    @Query("SELECT * FROM notes_table")
    fun getNotes() : Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes_table WHERE title LIKE :query")
    fun getNotesByQuery(query: String) : Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes_table WHERE id = :id")
    suspend fun findNote(id: Long) : NoteEntity

    @Update
    suspend fun updateNote(noteEntity: NoteEntity)

    @Insert
    suspend fun putNote(noteEntity: NoteEntity)
}