package com.example.noteslist.domain.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val stackMaxVisible: Flow<Int>
    val stackSpacing: Flow<Float>

    suspend fun setStackSpacing(newValue: Float)
    suspend fun setStackMaxVisible(newValue: Int)
}