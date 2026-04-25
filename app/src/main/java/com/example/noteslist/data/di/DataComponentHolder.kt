package com.example.noteslist.data.di

object DataComponentHolder {
    lateinit var component: DataComponent
        private set

    fun init(dependencies: DataComponent.Dependencies) {
        component = DataComponent(dependencies)
    }
}