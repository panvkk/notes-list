package com.example.noteslist.domain.usecase

import com.example.noteslist.domain.model.SettingsModel
import com.example.noteslist.domain.repository.ParametersRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class UpdateSettingsUseCase(private val repository: ParametersRepository) {
    suspend operator fun invoke(settings: SettingsModel) {
        repository.setStackMaxVisible(settings.stackMaxVisible)
        repository.setStackSpacing(settings.stackSpacing)
    }
}