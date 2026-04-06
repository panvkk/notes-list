package com.example.noteslist

import android.app.Application
import com.example.noteslist.data.local.NotesRepositoryImpl
import com.example.noteslist.domain.usecase.CreateNoteUseCase
import com.example.noteslist.domain.usecase.FindNoteUseCase
import com.example.noteslist.domain.usecase.NotesUseCase
import com.example.noteslist.domain.usecase.UpdateNoteUseCase

class NotesListApplication : Application() {
    val notesRepository by lazy { NotesRepositoryImpl() }
    val createNoteUseCase by lazy { CreateNoteUseCase(notesRepository) }
    val findNoteUseCase by lazy { FindNoteUseCase(notesRepository) }
    val updateNoteUseCase by lazy { UpdateNoteUseCase(notesRepository) }
    val notesUseCase by lazy { NotesUseCase(notesRepository) }

}