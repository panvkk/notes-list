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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.noteslist.R
import com.example.noteslist.presentation.ui.screen.NoteDetailsScreen
import com.example.noteslist.presentation.ui.theme.NotesListTheme
import com.example.noteslist.presentation.viewmodel.GlobalViewModel
import com.example.noteslist.presentation.viewmodel.NoteDetailsViewModel
import kotlinx.coroutines.launch

class NoteDetailsFragment : Fragment() {

    private val viewModel by viewModels<NoteDetailsViewModel> { NoteDetailsViewModel.factory }
    private val globalViewModel: GlobalViewModel by activityViewModels()
    private val args: NoteDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                NotesListTheme {
                    NoteDetailsScreen(
                        viewModel = viewModel,
                        onClickBack = {
                            val isTablet = requireActivity().findViewById<View>(R.id.detail_container) != null
                        if(isTablet) {
                            globalViewModel.selectNote(null)
                        } else {
                            findNavController().popBackStack()
                        }
                        }
                    )
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(arguments?.containsKey("noteId") == true) {
            val id = args.noteId.takeIf { it != -1L }
            viewModel.setNote(id)
        } else {
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    globalViewModel.selectedNoteId.collect { noteId ->
                        viewModel.setNote(noteId)
                    }
                }
            }
        }

        // слушаем, ввёл ли пользователь что-то новое
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.hasUnsavedChanges.collect { value ->
                    globalViewModel.hasUnsavedChanges = value
                }
            }
        }
    }
}