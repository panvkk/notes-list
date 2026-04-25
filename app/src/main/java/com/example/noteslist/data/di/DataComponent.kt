package com.example.noteslist.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.noteslist.data.local.NotesListDatabase
import com.example.noteslist.data.repository.NotesRepositoryImpl
import com.example.noteslist.data.repository.ParametersRepositoryImpl

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")

class DataComponent(
    private val dependencies: Dependencies
) {
    private val context: Context by lazy { dependencies.getContext() }
    private val database by lazy { NotesListDatabase.getDatabase(context) }

    val notesRepository by lazy { NotesRepositoryImpl(database.notesDao()) }
    val paramsRepository by lazy { ParametersRepositoryImpl(context.dataStore) }

    interface Dependencies {
        fun getContext() : Context
    }
}