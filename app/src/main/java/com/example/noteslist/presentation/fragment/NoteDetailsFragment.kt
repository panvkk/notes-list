package com.example.noteslist.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.noteslist.presentation.ui.screen.NoteDetailsScreen
import com.example.noteslist.presentation.viewmodel.NoteDetailsViewModel

class NoteDetailsFragment : Fragment() {

    private val viewModel by viewModels<NoteDetailsViewModel> { NoteDetailsViewModel.factory }

    private var noteId: Long? = null

    companion object {
        const val ARG_NOTE_ID = "arg_note_id"

        fun newInstance(noteId: Long?) : NoteDetailsFragment {
            return NoteDetailsFragment().apply {
                arguments = Bundle().apply {
                    if(noteId != null) {
                        putLong(ARG_NOTE_ID, noteId)
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        noteId = arguments?.getLong(ARG_NOTE_ID, -1L).takeIf { it != -1L }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                NoteDetailsScreen(
                    viewModel = viewModel,
                    onClickBack = {
                        findNavController()
                            .navigate(NoteDetailsFragmentDirections.returnToList())
                    }
                )
            }
        }
    }
}