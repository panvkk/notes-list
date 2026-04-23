package com.example.noteslist.data.repository

import com.example.noteslist.core.toLocalDate
import com.example.noteslist.core.toStringWithPattern
import com.example.noteslist.data.dto.NoteEntity
import com.example.noteslist.data.local.dao.NotesDao
import com.example.noteslist.domain.model.NoteModel
import com.example.noteslist.domain.repository.NotesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class NotesRepositoryImpl(
    private val notesDao: NotesDao
) : NotesRepository {

    override fun getNotes(): Flow<List<NoteModel>> {
        return notesDao.getNotes().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getNotesByQuery(query: String): Flow<List<NoteModel>> {
        return notesDao.getNotesByQuery(query).map { entities ->
            entities.map { it.toDomain() }
        }
    }
    override suspend fun createNote(title: String, description: String, date: LocalDate, isImportant: Boolean) {
        val noteEntity = NoteEntity(
            title = title,
            description = description,
            isImportant = isImportant,
            date = date.toStringWithPattern()
        )
        notesDao.putNote(noteEntity)
    }
    override suspend fun updateNote(note: NoteModel) : Result<Unit> {
        val noteEntity = note.toDto()
        return try {
            notesDao.updateNote(noteEntity)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun findNote(id: Long) : Result<NoteModel> {
        return try {
            val noteEntity = notesDao.findNote(id)
            Result.success(noteEntity.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun NoteEntity.toDomain() = NoteModel(
        id = id!!,
        title = title,
        description = description,
        isImportant = isImportant,
        date = date.toLocalDate(),
        isRead = isRead
    )

    fun NoteModel.toDto() = NoteEntity(
        id = id,
        title = title,
        description = description,
        isImportant = isImportant,
        date = date?.toStringWithPattern()
            ?: throw IllegalArgumentException("LocalDate is null."),
        isRead = isRead
    )
}