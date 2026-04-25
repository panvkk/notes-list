package com.example.noteslist.data.di

import android.content.Context

object DataComponentHolder {
    lateinit var component: DataComponent
        private set

    fun init(dependencies: DataComponent.Dependencies = DefaultDependencies()) {
        component = DataComponent(dependencies)
    }

    private class DefaultDependencies : DataComponent.Dependencies {
        override fun getContext(): Context {
            TODO("Not yet implemented")
        }
    }
}