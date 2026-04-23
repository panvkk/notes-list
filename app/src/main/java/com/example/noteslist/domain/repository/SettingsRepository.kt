package com.example.noteslist.domain.repository

interface SettingsRepository {
    suspend fun getStackSpacing() : Float
    suspend fun getStackMaxVisible() : Int

    suspend fun setStackSpacing(newValue: Float)
    suspend fun setStackMaxVisible(newValue: Int)
}