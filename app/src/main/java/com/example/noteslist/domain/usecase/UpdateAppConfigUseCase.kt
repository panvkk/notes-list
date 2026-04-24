package com.example.noteslist.domain.usecase

import com.example.noteslist.domain.model.AppConfig
import com.example.noteslist.domain.repository.ParametersRepository

class UpdateAppConfigUseCase(
    private val repository: ParametersRepository
) {
    suspend operator fun invoke(newAppConfig: AppConfig) {
        repository.setIsFirstEntry(newAppConfig.isFirstEntry)
    }
}