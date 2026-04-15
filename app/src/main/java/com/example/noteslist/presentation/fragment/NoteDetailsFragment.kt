package com.example.noteslist.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.noteslist.presentation.model.GlobalUiState
import com.example.noteslist.presentation.ui.screen.NoteDetailsScreen
import com.example.noteslist.presentation.ui.theme.NotesListTheme
import com.example.noteslist.presentation.viewmodel.GlobalViewModel
import com.example.noteslist.presentation.viewmodel.NoteDetailsViewModel
import kotlinx.coroutines.launch
import kotlin.getValue

class NoteDetailsFragment : Fragment() {

    private val viewModel by viewModels<NoteDetailsViewModel> { NoteDetailsViewModel.factory }
    private val globalViewModel: GlobalViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                NotesListTheme {NoteDetailsScreen(
                    viewModel = viewModel,
                    onClickSave = { globalViewModel.closeDetails() } ,
                    onClickCancel = { requireActivity().onBackPressedDispatcher.onBackPressed() }
                )}
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                globalViewModel.uiState.collect { state ->
                    when(state) {
                        is GlobalUiState.CreateNote -> viewModel.setNote(null)
                        is GlobalUiState.EditNote ->  viewModel.setNote(state.noteId)
                        else -> {  }
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.hasUnsavedChanges.collect { state ->
                    globalViewModel.hasUnsavedChanges = state
                }
            }
        }
    }
}