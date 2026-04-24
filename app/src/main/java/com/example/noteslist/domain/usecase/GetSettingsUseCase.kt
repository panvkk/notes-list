package com.example.noteslist.domain.usecase

import com.example.noteslist.domain.model.SettingsModel
import com.example.noteslist.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first

class GetSettingsUseCase(
    private val repository: SettingsRepository
) {
    suspend fun invokeFirst() : SettingsModel {
        return SettingsModel(
            repository.stackSpacing.first(),
            repository.stackMaxVisible.first()
        )
    }
    fun invokeFlow() : Flow<SettingsModel> {
        return combine(repository.stackSpacing, repository.stackMaxVisible) { stackSpacing, stackMaxVisible ->
            SettingsModel(stackSpacing, stackMaxVisible)
        }
    }
}