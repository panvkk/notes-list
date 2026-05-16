package com.example.noteslist.presentation.di

import com.example.noteslist.presentation.di.subcomponent.NoteDetailsSubComponent
import com.example.noteslist.presentation.di.subcomponent.NotesListSubComponent
import com.example.noteslist.presentation.di.subcomponent.SettingsSubComponent

object PresentationComponentHolder {
    lateinit var presentationComponent: PresentationComponent
        private set

    private var noteDetailsSubComponent: NoteDetailsSubComponent? = null
    private var notesListSubComponent: NotesListSubComponent? = null
    private var settingsSubComponent: SettingsSubComponent? = null

    fun init(dependencies: PresentationComponent.Dependencies) {
        presentationComponent = PresentationComponent(dependencies)
    }

    fun getNoteDetailsSubComponent() : NoteDetailsSubComponent {
        return noteDetailsSubComponent ?: presentationComponent.provideNoteDetailsSubComponent()
            .also { noteDetailsSubComponent = it }
    }
    fun getNotesListSubComponent() : NotesListSubComponent {
        return notesListSubComponent ?: presentationComponent.provideNotesListSubComponent()
            .also { notesListSubComponent = it }
    }
    fun getSettingsSubComponent() : SettingsSubComponent {
        return settingsSubComponent ?: presentationComponent.provideSettingsSubComponent()
            .also { settingsSubComponent = it }
    }

    fun clearNoteDetailsSubComponent() {
        noteDetailsSubComponent = null
    }
    fun clearNotesListSubComponent() {
        notesListSubComponent = null
    }
    fun clearSettingsSubComponent() {
        settingsSubComponent = null
    }
}