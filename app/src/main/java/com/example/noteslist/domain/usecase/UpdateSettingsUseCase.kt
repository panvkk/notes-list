package com.example.noteslist.domain.usecase

import com.example.noteslist.domain.model.SettingsModel
import com.example.noteslist.domain.repository.SettingsRepository

class UpdateSettingsUseCase(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(settings: SettingsModel) {
        repository.setStackMaxVisible(settings.stackMaxVisible)
        repository.setStackSpacing(settings.stackSpacing)
    }
}