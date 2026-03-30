package com.example.noteslist.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.noteslist.presentation.ui.screen.NoteDetailsScreen
import com.example.noteslist.presentation.viewmodel.NoteDetailsViewModel

class NoteDetailsFragment : Fragment() {

    private val viewModel by viewModels<NoteDetailsViewModel> { NoteDetailsViewModel.factory }

    private val args: NoteDetailsFragmentArgs by navArgs()
    private var noteId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noteId = args.noteId.takeIf { it != -1L }
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setCurrentNote(noteId)
    }
}