package com.example.noteslist.presentation.di

import com.example.noteslist.domain.di.DomainComponentHolder
import com.example.noteslist.domain.usecase.CreateNoteUseCase
import com.example.noteslist.domain.usecase.FindNoteUseCase
import com.example.noteslist.domain.usecase.GetAppConfigUseCase
import com.example.noteslist.domain.usecase.GetSettingsUseCase
import com.example.noteslist.domain.usecase.NotesUseCase
import com.example.noteslist.domain.usecase.UpdateAppConfigUseCase
import com.example.noteslist.domain.usecase.UpdateNoteReadUseCase
import com.example.noteslist.domain.usecase.UpdateNoteUseCase
import com.example.noteslist.domain.usecase.UpdateSettingsUseCase

object PresentationComponentHolder {
    lateinit var component: PresentationComponent
        private set

    fun init(dependencies: PresentationComponent.Dependencies = DefaultDependencies()) {
        component = PresentationComponent(dependencies)
    }

    private class DefaultDependencies : PresentationComponent.Dependencies {
        override fun getCreateNoteUseCase(): CreateNoteUseCase {
            return DomainComponentHolder.component.createNoteUseCase()
        }
        override fun getFindNoteUseCase(): FindNoteUseCase {
            return DomainComponentHolder.component.findNoteUseCase()
        }
        override fun getGetAppConfigUseCase(): GetAppConfigUseCase {
            return DomainComponentHolder.component.getAppConfigUseCase()
        }
        override fun getUpdateSettingsUseCase(): UpdateSettingsUseCase {
            return DomainComponentHolder.component.updateSettingsUseCase()
        }
        override fun getGetSettingsUseCase(): GetSettingsUseCase {
            return DomainComponentHolder.component.getSettingsUseCase()
        }
        override fun getNotesUseCase(): NotesUseCase {
            return DomainComponentHolder.component.notesUseCase()
        }
        override fun getUpdateNoteUseCase(): UpdateNoteUseCase {
            return DomainComponentHolder.component.updateNoteUseCase()
        }
        override fun getUpdateAppConfigUseCase(): UpdateAppConfigUseCase {
            return DomainComponentHolder.component.updateAppConfigUseCase()
        }
        override fun getUpdateNoteReadUseCase(): UpdateNoteReadUseCase {
            return DomainComponentHolder.component.updateNoteReadUseCase()
        }
    }
}