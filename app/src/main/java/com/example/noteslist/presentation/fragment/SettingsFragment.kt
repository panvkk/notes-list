package com.example.noteslist.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.noteslist.core.presentation.showOnExitAlert
import com.example.noteslist.core.presentation.showUnsavedChangesAlert
import com.example.noteslist.presentation.model.GlobalUiState
import com.example.noteslist.presentation.ui.screen.NoteDetailsScreen
import com.example.noteslist.presentation.ui.screen.SettingsScreen
import com.example.noteslist.presentation.ui.theme.NotesListTheme
import com.example.noteslist.presentation.viewmodel.SettingsViewModel

internal class SettingsFragment : Fragment() {

    private val viewModel: SettingsViewModel = SettingsViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                NotesListTheme {
                    SettingsScreen(
                        viewModel = viewModel,
                        onClickCancel = { findNavController().popBackStack() },
                        onClickSave = {
                            findNavController().navigate(SettingsFragmentDirections.openList())
                        }
                    )
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            backPressedCallback)
    }

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            findNavController().popBackStack()
        }
    }
}