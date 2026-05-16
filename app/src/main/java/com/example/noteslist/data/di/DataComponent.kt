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
    private val context: Context by lazy { dependencies.getContext().applicationContext }

    val notesRepository by lazy {
        NotesRepositoryImpl(
            NotesListDatabase.getDatabase(context).notesDao()
        )
    }
    val paramsRepository by lazy { ParametersRepositoryImpl(context.dataStore) }

    interface Dependencies {
        fun getContext() : Context
    }
}