package com.example.noteslist.domain.di

import com.example.noteslist.data.di.DataComponentHolder
import com.example.noteslist.domain.repository.NotesRepository
import com.example.noteslist.domain.repository.ParametersRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

object DomainComponentHolder {
    lateinit var component: DomainComponent
        private set

    fun init(dependencies: DomainComponent.Dependencies = DefaultDependencies()) {
        component = DomainComponent(dependencies)
    }

    private class DefaultDependencies : DomainComponent.Dependencies {
        override fun getNotesRepository(): NotesRepository {
            return DataComponentHolder.component.notesRepository
        }

        override fun getParametersRepository(): ParametersRepository {
            return DataComponentHolder.component.paramsRepository
        }

        override fun getApplicationScope(): CoroutineScope {
            return CoroutineScope(SupervisorJob() + Dispatchers.IO)
        }
    }
}