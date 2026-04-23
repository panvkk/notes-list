package com.example.noteslist

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.noteslist.data.local.NotesListDatabase
import com.example.noteslist.data.repository.NotesRepositoryImpl
import com.example.noteslist.domain.usecase.CreateNoteUseCase
import com.example.noteslist.domain.usecase.FindNoteUseCase
import com.example.noteslist.domain.usecase.NotesUseCase
import com.example.noteslist.domain.usecase.UpdateNoteReadUseCase
import com.example.noteslist.domain.usecase.UpdateNoteUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class NotesListApplication : Application() {

    val database by lazy {
        NotesListDatabase.getDatabase(
            this,
            CoroutineScope(SupervisorJob() + Dispatchers.Main)
        )
    }

    val notesRepository by lazy { NotesRepositoryImpl(database.notesDao()) }
    val createNoteUseCase by lazy { CreateNoteUseCase(notesRepository) }
    val findNoteUseCase by lazy { FindNoteUseCase(notesRepository) }
    val updateNoteUseCase by lazy { UpdateNoteUseCase(notesRepository) }
    val updateNoteReadUseCase by lazy { UpdateNoteReadUseCase(findNoteUseCase, updateNoteUseCase) }
    val notesUseCase by lazy { NotesUseCase(notesRepository) }

}