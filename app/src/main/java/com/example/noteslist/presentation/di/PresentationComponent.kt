package com.example.noteslist.presentation.di

import com.example.noteslist.domain.usecase.CreateNoteUseCase
import com.example.noteslist.domain.usecase.FindNoteUseCase
import com.example.noteslist.domain.usecase.GetAppConfigUseCase
import com.example.noteslist.domain.usecase.GetSettingsUseCase
import com.example.noteslist.domain.usecase.NotesUseCase
import com.example.noteslist.domain.usecase.UpdateAppConfigUseCase
import com.example.noteslist.domain.usecase.UpdateNoteReadUseCase
import com.example.noteslist.domain.usecase.UpdateNoteUseCase
import com.example.noteslist.domain.usecase.UpdateSettingsUseCase
import com.example.noteslist.presentation.di.subcomponent.NoteDetailsSubComponent
import com.example.noteslist.presentation.di.subcomponent.NotesListSubComponent
import com.example.noteslist.presentation.di.subcomponent.SettingsSubComponent
import kotlinx.coroutines.CoroutineScope

class PresentationComponent(
    private val dependencies: Dependencies
) {
    fun provideNoteDetailsSubComponent() = NoteDetailsSubComponent(
        object : NoteDetailsSubComponent.Dependencies {
            override fun getApplicationScope(): CoroutineScope = dependencies.getApplicationScope()
            override fun getCreateNoteUseCase(): CreateNoteUseCase = dependencies.getCreateNoteUseCase()
            override fun getFindNoteUseCase(): FindNoteUseCase = dependencies.getFindNoteUseCase()
            override fun getUpdateNoteUseCase(): UpdateNoteUseCase = dependencies.getUpdateNoteUseCase()
        }
    )

    fun provideNotesListSubComponent() = NotesListSubComponent(
        object : NotesListSubComponent.Dependencies {
            override fun getApplicationScope(): CoroutineScope = dependencies.getApplicationScope()
            override fun getGetAppConfigUseCase(): GetAppConfigUseCase = dependencies.getGetAppConfigUseCase()
            override fun getGetSettingsUseCase(): GetSettingsUseCase = dependencies.getGetSettingsUseCase()
            override fun getNotesUseCase(): NotesUseCase = dependencies.getNotesUseCase()
            override fun getUpdateNoteReadUseCase(): UpdateNoteReadUseCase =
                dependencies.getUpdateNoteReadUseCase()
            override fun getUpdateAppConfigUseCase(): UpdateAppConfigUseCase =
                dependencies.getUpdateAppConfigUseCase()
        }
    )

    fun provideSettingsSubComponent() = SettingsSubComponent(
        object : SettingsSubComponent.Dependencies {
            override fun getApplicationScope(): CoroutineScope = dependencies.getApplicationScope()
            override fun getGetSettingsUseCase(): GetSettingsUseCase = dependencies.getGetSettingsUseCase()
            override fun getUpdateSettingsUseCase(): UpdateSettingsUseCase =
                dependencies.getUpdateSettingsUseCase()
        }
    )

    interface Dependencies {
        fun getApplicationScope() : CoroutineScope
        fun getCreateNoteUseCase() : CreateNoteUseCase
        fun getFindNoteUseCase() : FindNoteUseCase
        fun getUpdateNoteUseCase() : UpdateNoteUseCase
        fun getUpdateNoteReadUseCase() : UpdateNoteReadUseCase
        fun getNotesUseCase() : NotesUseCase

        fun getGetSettingsUseCase() : GetSettingsUseCase
        fun getUpdateSettingsUseCase() : UpdateSettingsUseCase
        fun getGetAppConfigUseCase() : GetAppConfigUseCase
        fun getUpdateAppConfigUseCase() : UpdateAppConfigUseCase
    }
}