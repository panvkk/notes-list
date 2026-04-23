package com.example.noteslist.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import com.example.noteslist.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class SettingsRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : SettingsRepository {

    override suspend fun getStackMaxVisible(): Int {
        return dataStore.data.first()[STACK_MAX_VISIBLE_KEY] ?: DEFAULT_STACK_MAX_VISIBLE
    }
    override suspend fun getStackSpacing(): Float {
        return dataStore.data.first()[STACK_SPACING_KEY] ?: DEFAULT_STACK_SPACING
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

    companion object {
        private val STACK_SPACING_KEY = floatPreferencesKey("stack_spacing")
        private val STACK_MAX_VISIBLE_KEY = intPreferencesKey("stack_max_visible")

        private const val DEFAULT_STACK_MAX_VISIBLE = 3
        private const val DEFAULT_STACK_SPACING = 50f
    }
}