package com.example.noteslist.presentation.di.subcomponent

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.noteslist.domain.usecase.GetSettingsUseCase
import com.example.noteslist.domain.usecase.UpdateSettingsUseCase
import com.example.noteslist.presentation.viewmodel.SettingsViewModel

class SettingsSubComponent(
    private val dependencies: Dependencies
) {
    fun createSettingsViewModelFactory() : ViewModelProvider.Factory {
        return viewModelFactory { initializer { createSettingsViewModel() } }
    }

    private fun createSettingsViewModel() : SettingsViewModel {
        return SettingsViewModel(
            dependencies.getGetSettingsUseCase(),
            dependencies.getUpdateSettingsUseCase()
        )
    }

    interface Dependencies {
        fun getGetSettingsUseCase() : GetSettingsUseCase
        fun getUpdateSettingsUseCase() : UpdateSettingsUseCase
    }
}