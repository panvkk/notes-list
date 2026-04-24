package com.example.noteslist

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.noteslist.data.local.NotesListDatabase
import com.example.noteslist.data.repository.NotesRepositoryImpl
import com.example.noteslist.data.repository.SettingsRepositoryImpl
import com.example.noteslist.domain.usecase.CreateNoteUseCase
import com.example.noteslist.domain.usecase.FindNoteUseCase
import com.example.noteslist.domain.usecase.GetSettingsUseCase
import com.example.noteslist.domain.usecase.NotesUseCase
import com.example.noteslist.domain.usecase.UpdateNoteReadUseCase
import com.example.noteslist.domain.usecase.UpdateNoteUseCase
import com.example.noteslist.domain.usecase.UpdateSettingsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")
class NotesListApplication : Application() {

    val database by lazy { NotesListDatabase.getDatabase(this) }

    private val notesRepository by lazy { NotesRepositoryImpl(database.notesDao()) }
    private val settingsRepository by lazy { SettingsRepositoryImpl(dataStore) }
    val createNoteUseCase by lazy { CreateNoteUseCase(notesRepository) }
    val findNoteUseCase by lazy { FindNoteUseCase(notesRepository) }
    val updateNoteUseCase by lazy { UpdateNoteUseCase(notesRepository) }
    val updateNoteReadUseCase by lazy { UpdateNoteReadUseCase(findNoteUseCase, updateNoteUseCase) }
    val notesUseCase by lazy { NotesUseCase(notesRepository) }
    val getSettingsUseCase by lazy { GetSettingsUseCase(settingsRepository) }
    val updateSettingsUseCase by lazy { UpdateSettingsUseCase(settingsRepository) }
}