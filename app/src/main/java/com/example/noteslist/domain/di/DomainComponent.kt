package com.example.noteslist.domain.di

import com.example.noteslist.domain.repository.NotesRepository
import com.example.noteslist.domain.repository.ParametersRepository
import com.example.noteslist.domain.usecase.CreateNoteUseCase
import com.example.noteslist.domain.usecase.FindNoteUseCase
import com.example.noteslist.domain.usecase.GetAppConfigUseCase
import com.example.noteslist.domain.usecase.GetSettingsUseCase
import com.example.noteslist.domain.usecase.NotesUseCase
import com.example.noteslist.domain.usecase.UpdateAppConfigUseCase
import com.example.noteslist.domain.usecase.UpdateNoteReadUseCase
import com.example.noteslist.domain.usecase.UpdateNoteUseCase
import com.example.noteslist.domain.usecase.UpdateSettingsUseCase

class DomainComponent(
    private val dependencies: Dependencies
) {

    fun createNoteUseCase(): CreateNoteUseCase =
        CreateNoteUseCase(dependencies.getNotesRepository())
    fun findNoteUseCase(): FindNoteUseCase =
        FindNoteUseCase(dependencies.getNotesRepository())
    fun updateNoteUseCase(): UpdateNoteUseCase =
        UpdateNoteUseCase(dependencies.getNotesRepository())
    fun updateNoteReadUseCase(): UpdateNoteReadUseCase =
        UpdateNoteReadUseCase(
            findNoteUseCase = findNoteUseCase(),
            updateNoteUseCase = updateNoteUseCase()
        )

    fun notesUseCase(): NotesUseCase =
        NotesUseCase(dependencies.getNotesRepository())

    fun getSettingsUseCase(): GetSettingsUseCase =
        GetSettingsUseCase(dependencies.getParametersRepository())
    fun updateSettingsUseCase(): UpdateSettingsUseCase =
        UpdateSettingsUseCase(dependencies.getParametersRepository())

    fun getAppConfigUseCase(): GetAppConfigUseCase =
        GetAppConfigUseCase(dependencies.getParametersRepository())
    fun updateAppConfigUseCase(): UpdateAppConfigUseCase =
        UpdateAppConfigUseCase(dependencies.getParametersRepository())

    interface Dependencies {
        fun getNotesRepository() : NotesRepository
        fun getParametersRepository() : ParametersRepository
    }
}