package com.example.noteslist

import android.app.Application
import android.content.Context
import com.example.noteslist.data.di.DataComponent
import com.example.noteslist.data.di.DataComponentHolder
import com.example.noteslist.domain.di.DomainComponent
import com.example.noteslist.domain.di.DomainComponentHolder
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
import com.example.noteslist.presentation.di.PresentationComponent
import com.example.noteslist.presentation.di.PresentationComponentHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class NotesListApplication : Application() {
    val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        DataComponentHolder.init(
            object : DataComponent.Dependencies {
                override fun getContext(): Context = this@NotesListApplication
            }
        )
        DomainComponentHolder.init(
            object : DomainComponent.Dependencies {
                override fun getNotesRepository(): NotesRepository = DataComponentHolder.component.notesRepository
                override fun getParametersRepository(): ParametersRepository = DataComponentHolder.component.paramsRepository
            }
        )
        PresentationComponentHolder.init(
            object : PresentationComponent.Dependencies {
                override fun getApplicationScope(): CoroutineScope = applicationScope
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
        )
    }
}