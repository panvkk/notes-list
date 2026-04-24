package com.example.noteslist

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.noteslist.data.local.NotesListDatabase
import com.example.noteslist.data.repository.NotesRepositoryImpl
import com.example.noteslist.data.repository.ParametersRepositoryImpl
import com.example.noteslist.domain.usecase.CreateNoteUseCase
import com.example.noteslist.domain.usecase.FindNoteUseCase
import com.example.noteslist.domain.usecase.GetAppConfigUseCase
import com.example.noteslist.domain.usecase.GetSettingsUseCase
import com.example.noteslist.domain.usecase.NotesUseCase
import com.example.noteslist.domain.usecase.UpdateAppConfigUseCase
import com.example.noteslist.domain.usecase.UpdateNoteReadUseCase
import com.example.noteslist.domain.usecase.UpdateNoteUseCase
import com.example.noteslist.domain.usecase.UpdateSettingsUseCase

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")
class NotesListApplication : Application() {

    val database by lazy { NotesListDatabase.getDatabase(this) }

    private val notesRepository by lazy { NotesRepositoryImpl(database.notesDao()) }
    private val paramsRepository by lazy { ParametersRepositoryImpl(dataStore) }

    val createNoteUseCase by lazy { CreateNoteUseCase(notesRepository) }
    val findNoteUseCase by lazy { FindNoteUseCase(notesRepository) }
    val updateNoteUseCase by lazy { UpdateNoteUseCase(notesRepository) }
    val updateNoteReadUseCase by lazy { UpdateNoteReadUseCase(findNoteUseCase, updateNoteUseCase) }
    val notesUseCase by lazy { NotesUseCase(notesRepository) }
    val getSettingsUseCase by lazy { GetSettingsUseCase(paramsRepository) }
    val updateSettingsUseCase by lazy { UpdateSettingsUseCase(paramsRepository) }

    val getAppConfigUseCase by lazy { GetAppConfigUseCase(paramsRepository) }
    val updateAppConfigUseCase by lazy { UpdateAppConfigUseCase(paramsRepository) }
}