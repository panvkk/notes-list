package com.example.noteslist

import android.app.Application
import android.content.Context
import com.example.noteslist.data.di.DataComponent
import com.example.noteslist.data.di.DataComponentHolder
import com.example.noteslist.domain.di.DomainComponentHolder
import com.example.noteslist.presentation.di.PresentationComponentHolder

class NotesListApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        DataComponentHolder.init(
            object : DataComponent.Dependencies {
                override fun getContext(): Context = this@NotesListApplication
            }
        )
        DomainComponentHolder.init()
        PresentationComponentHolder.init()
    }
}