package com.example.noteslist.domain.di

object DomainComponentHolder {
    lateinit var component: DomainComponent
        private set

    fun init(dependencies: DomainComponent.Dependencies) {
        component = DomainComponent(dependencies)
    }
}