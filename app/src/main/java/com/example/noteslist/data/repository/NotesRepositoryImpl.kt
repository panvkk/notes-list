package com.example.noteslist.data.repository

import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteFullException
import com.example.noteslist.core.Resource
import com.example.noteslist.core.domain.error.DomainError
import com.example.noteslist.core.toLocalDate
import com.example.noteslist.core.toStringWithPattern
import com.example.noteslist.data.dto.NoteEntity
import com.example.noteslist.data.local.dao.NotesDao
import com.example.noteslist.domain.model.NoteModel
import com.example.noteslist.domain.repository.NotesRepository
import kotlinx.coroutines.CancellationException
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
    override suspend fun createNote(
        title: String,
        description: String,
        date: LocalDate,
        isImportant: Boolean
    ) : Resource<Unit> {
        return try {
            val noteEntity = NoteEntity(
                title = title,
                description = description,
                isImportant = isImportant,
                date = date.toStringWithPattern()
            )
            notesDao.putNote(noteEntity)
            Resource.Success(Unit)
        } catch (e: SQLiteFullException) {
            Resource.Error(DomainError.StorageError.NoDiskSpace)
        } catch (e: SQLiteException) {
            Resource.Error(DomainError.StorageError.Other(e.message))
        } catch (e: Exception) {
            if(e is CancellationException) throw e

            Resource.Error(DomainError.UnexpectedException(e))
        }

    }
    override suspend fun updateNote(note: NoteModel) : Resource<Unit> {
        val noteEntity = note.toDto()
        return try {
            notesDao.updateNote(noteEntity)
            Resource.Success(Unit)
        } catch (e: SQLiteFullException) {
            Resource.Error(DomainError.StorageError.NoDiskSpace)
        } catch (e: SQLiteException) {
            Resource.Error(DomainError.StorageError.Other(e.message))
        } catch (e: Exception) {
            if(e is CancellationException) throw e

            Resource.Error(DomainError.UnexpectedException(e))
        }
    }

    override suspend fun findNote(id: Long) : Resource<NoteModel> {
        return try {
            val noteEntity = notesDao.findNote(id)
            Resource.Success(noteEntity.toDomain())
        } catch (e: SQLiteException) {
            Resource.Error(DomainError.StorageError.Other(e.message))
        } catch (e: Exception) {
            if(e is CancellationException) throw e

            Resource.Error(DomainError.UnexpectedException(e))
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