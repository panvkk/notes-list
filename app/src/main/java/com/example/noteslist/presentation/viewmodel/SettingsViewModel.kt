package com.example.noteslist.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteslist.domain.model.SettingsModel
import com.example.noteslist.domain.usecase.GetSettingsUseCase
import com.example.noteslist.domain.usecase.UpdateSettingsUseCase
import com.example.noteslist.presentation.di.PresentationComponentHolder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val getSettingsUseCase: GetSettingsUseCase,
    private val updateSettingsUseCase: UpdateSettingsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsModel(40f, 2))
    val uiState = _uiState.asStateFlow()

    init { fetchSettings() }

    fun updateStackSpacing(value: Float) {
        _uiState.update { it.copy(stackSpacing = value) }
    }
    fun increaseStackMaxVisible() {
        _uiState.update { it.copy(stackMaxVisible = _uiState.value.stackMaxVisible + 1) }
    }
    fun decreaseStackMaxVisible() {
        _uiState.update { it.copy(stackMaxVisible = _uiState.value.stackMaxVisible - 1) }
    }

    fun saveSettings() {
        updateSettingsUseCase.invoke(_uiState.value)
    }

    private fun fetchSettings() {
        viewModelScope.launch {
            val settings = getSettingsUseCase.invokeFirst()
            _uiState.update { settings }
        }
    }

    override fun onCleared() {
        super.onCleared()
        PresentationComponentHolder.clearSettingsSubComponent()
    }
}