package com.example.noteslist.presentation.di.subcomponent

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.noteslist.domain.usecase.GetAppConfigUseCase
import com.example.noteslist.domain.usecase.GetSettingsUseCase
import com.example.noteslist.domain.usecase.NotesUseCase
import com.example.noteslist.domain.usecase.UpdateAppConfigUseCase
import com.example.noteslist.domain.usecase.UpdateNoteReadUseCase
import com.example.noteslist.presentation.viewmodel.NotesListViewModel

class NotesListSubComponent(
    private val dependencies: Dependencies
) {
    fun createNotesListViewModelFactory() : ViewModelProvider.Factory {
        return viewModelFactory { initializer { createNotesListViewModel() } }
    }

    private fun createNotesListViewModel() : NotesListViewModel {
        return NotesListViewModel(
            dependencies.getNotesUseCase(),
            dependencies.getUpdateNoteReadUseCase(),
            dependencies.getGetSettingsUseCase(),
            dependencies.getGetAppConfigUseCase(),
            dependencies.getUpdateAppConfigUseCase()
        )
    }

    interface Dependencies {
        fun getNotesUseCase() : NotesUseCase
        fun getUpdateNoteReadUseCase() : UpdateNoteReadUseCase
        fun getGetSettingsUseCase() : GetSettingsUseCase
        fun getGetAppConfigUseCase() : GetAppConfigUseCase
        fun getUpdateAppConfigUseCase() : UpdateAppConfigUseCase
    }
}