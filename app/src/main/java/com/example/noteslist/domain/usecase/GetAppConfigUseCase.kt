package com.example.noteslist.domain.usecase

import com.example.noteslist.domain.model.AppConfig
import com.example.noteslist.domain.repository.ParametersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetAppConfigUseCase(
    private val repository: ParametersRepository
) {
    operator fun invoke(): Flow<AppConfig> =
        repository.isFirstEntry.map { AppConfig(it) }
}