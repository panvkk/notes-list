package com.example.noteslist.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.noteslist.presentation.model.SettingsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SettingsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsUiState(20f, 2))
    val uiState = _uiState.asStateFlow()

    fun updateStackSpacing(value: Float) {
        _uiState.update { it.copy(stackSpacing = value) }
    }
    fun increaseStackMaxVisible() {
        _uiState.update { it.copy(stackMaxVisible = _uiState.value.stackMaxVisible + 1) }
    }
    fun decreaseStackMaxVisible() {
        _uiState.update { it.copy(stackMaxVisible = _uiState.value.stackMaxVisible - 1) }
    }
}