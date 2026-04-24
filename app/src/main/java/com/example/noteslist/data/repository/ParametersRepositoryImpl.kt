package com.example.noteslist.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import com.example.noteslist.domain.repository.ParametersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ParametersRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : ParametersRepository {

    override val stackMaxVisible: Flow<Int> = dataStore.data.map { preferences ->
        preferences[STACK_MAX_VISIBLE_KEY] ?: DEFAULT_STACK_MAX_VISIBLE
    }
    override val stackSpacing: Flow<Float> = dataStore.data.map { preferences ->
        preferences[STACK_SPACING_KEY] ?: DEFAULT_STACK_SPACING
    }
    override val isFirstEntry: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[IS_FIRST_ENTRY] ?: DEFAULT_IS_FIRST_ENTRY
    }


    override suspend fun setStackMaxVisible(newValue: Int) {
        dataStore.edit { preferences ->
            preferences[STACK_MAX_VISIBLE_KEY] = newValue
        }
    }
    override suspend fun setStackSpacing(newValue: Float) {
        dataStore.edit { preferences ->
            preferences[STACK_SPACING_KEY] = newValue
        }
    }
    override suspend fun setIsFirstEntry(newValue: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_FIRST_ENTRY] = newValue
        }
    }

    companion object {
        private val STACK_SPACING_KEY = floatPreferencesKey("stack_spacing")
        private val STACK_MAX_VISIBLE_KEY = intPreferencesKey("stack_max_visible")
        private val IS_FIRST_ENTRY = booleanPreferencesKey("is_first_entry")

        private const val DEFAULT_IS_FIRST_ENTRY = true
        private const val DEFAULT_STACK_MAX_VISIBLE = 3
        private const val DEFAULT_STACK_SPACING = 50f
    }
}