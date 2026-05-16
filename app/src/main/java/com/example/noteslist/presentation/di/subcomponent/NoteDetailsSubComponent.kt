package com.example.noteslist.presentation.di.subcomponent

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.noteslist.domain.usecase.CreateNoteUseCase
import com.example.noteslist.domain.usecase.FindNoteUseCase
import com.example.noteslist.domain.usecase.UpdateNoteUseCase
import com.example.noteslist.presentation.viewmodel.NoteDetailsViewModel
import kotlinx.coroutines.CoroutineScope

class NoteDetailsSubComponent(
    private val dependencies: Dependencies
) {
    fun createNoteDetailsViewModelFactory() : ViewModelProvider.Factory {
        return viewModelFactory {
            initializer {
                createNoteDetailsViewModel(createSavedStateHandle())
            }
        }
    }

    private fun createNoteDetailsViewModel(savedStateHandle: SavedStateHandle) : NoteDetailsViewModel {
        return NoteDetailsViewModel(
            savedStateHandle,
            dependencies.getCreateNoteUseCase(),
            dependencies.getUpdateNoteUseCase(),
            dependencies.getFindNoteUseCase(),
            dependencies.getApplicationScope()
        )
    }

    interface Dependencies {
        fun getCreateNoteUseCase() : CreateNoteUseCase
        fun getUpdateNoteUseCase() : UpdateNoteUseCase
        fun getFindNoteUseCase() : FindNoteUseCase
        fun getApplicationScope() : CoroutineScope
    }
}