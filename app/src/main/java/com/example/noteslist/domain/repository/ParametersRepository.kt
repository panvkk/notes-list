package com.example.noteslist.domain.repository

import kotlinx.coroutines.flow.Flow

interface ParametersRepository {
    val stackMaxVisible: Flow<Int>
    val stackSpacing: Flow<Float>
    val isFirstEntry: Flow<Boolean>

    suspend fun setStackSpacing(newValue: Float)
    suspend fun setStackMaxVisible(newValue: Int)
    suspend fun setIsFirstEntry(newValue: Boolean)
}