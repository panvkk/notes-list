package com.example.noteslist.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.noteslist.presentation.model.SettingsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SettingsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsUiState(1, 1))
    val uiState = _uiState.asStateFlow()

    fun increaseStackSpacing() {
        _uiState.update { it.copy(stackSpacing = _uiState.value.stackSpacing + 1) }
    }
    fun decreaseStackSpacing() {
        _uiState.update { it.copy(stackSpacing = _uiState.value.stackSpacing - 1) }
    }

    fun increaseStackMaxVisible() {
        _uiState.update { it.copy(stackMaxVisible = _uiState.value.stackMaxVisible + 1) }
    }
    fun decreaseStackMaxVisible() {
        _uiState.update { it.copy(stackMaxVisible = _uiState.value.stackMaxVisible - 1) }
    }
}