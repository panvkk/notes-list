package com.example.noteslist

import android.app.Application
import android.content.Context
import com.example.noteslist.data.di.DataComponent
import com.example.noteslist.data.di.DataComponentHolder
import com.example.noteslist.domain.di.DomainComponent
import com.example.noteslist.domain.di.DomainComponentHolder
import com.example.noteslist.domain.repository.NotesRepository
import com.example.noteslist.domain.repository.ParametersRepository
import com.example.noteslist.presentation.di.PresentationComponentHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class NotesListApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        DataComponentHolder.init(
            object : DataComponent.Dependencies {
                override fun getContext(): Context = this@NotesListApplication
            }
        )
        DomainComponentHolder.init(
            object : DomainComponent.Dependencies {
                override fun getApplicationScope(): CoroutineScope = applicationScope
                override fun getNotesRepository(): NotesRepository = DataComponentHolder.component.notesRepository
                override fun getParametersRepository(): ParametersRepository = DataComponentHolder.component.paramsRepository
            }
        )
        PresentationComponentHolder.init()
    }
}