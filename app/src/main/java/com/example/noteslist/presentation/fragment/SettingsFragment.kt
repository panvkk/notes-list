package com.example.noteslist.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.noteslist.presentation.di.PresentationComponentHolder
import com.example.noteslist.presentation.di.subcomponent.SettingsSubComponent
import com.example.noteslist.presentation.ui.screen.SettingsScreen
import com.example.noteslist.presentation.ui.theme.NotesListTheme
import com.example.noteslist.presentation.viewmodel.SettingsViewModel

internal class SettingsFragment : Fragment() {
    private val viewModel by viewModels<SettingsViewModel> {
        PresentationComponentHolder.getSettingsSubComponent().createSettingsViewModelFactory()
    }

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
                            viewModel.saveSettings()
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