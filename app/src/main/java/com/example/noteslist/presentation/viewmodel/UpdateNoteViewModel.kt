package com.example.noteslist.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.noteslist.domain.usecase.UpdateNoteUseCase
import com.example.noteslist.presentation.model.ViewTyped
import kotlinx.coroutines.flow.MutableStateFlow

class UpdateNoteViewModel(
    private val updateNoteUseCase: UpdateNoteUseCase = UpdateNoteUseCase(),
) : ViewModel()  {

}