package com.example.noteslist.domain.usecase

import com.example.noteslist.domain.model.SettingsModel
import com.example.noteslist.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetSettingsUseCase(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke() : SettingsModel {
        return SettingsModel(repository.getStackSpacing(), repository.getStackMaxVisible())
    }
}