package com.example.noteslist.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.slidingpanelayout.widget.SlidingPaneLayout
import com.example.noteslist.core.presentation.showOnExitAlert
import com.example.noteslist.core.presentation.showUnsavedChangesAlert
import com.example.noteslist.databinding.FragmentMainHostBinding
import com.example.noteslist.presentation.model.GlobalUiState
import com.example.noteslist.presentation.viewmodel.GlobalViewModel
import kotlinx.coroutines.launch

class MainHostFragment : Fragment() {
    private var _binding: FragmentMainHostBinding? = null
    private val binding get() = _binding!!

    private val globalViewModel: GlobalViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainHostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            backPressedCallback
        )

        val slidingPane = binding.slidingPaneLayout
        slidingPane.lockMode = SlidingPaneLayout.LOCK_MODE_LOCKED // TODO узнать в чём же проблема редких зависаний

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                globalViewModel.uiState.collect { state ->
                    when(state) {
                        is GlobalUiState.CreateNote -> slidingPane.openPane()
                        is GlobalUiState.EditNote -> slidingPane.openPane()
                        is GlobalUiState.Idle -> slidingPane.closePane()
                    }
                }
            }
        }
    }

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val slidingPane = binding.slidingPaneLayout

            // Открыта заметка и это портретный режим - 1 случай
            // Открыта заметка и ландшафтный режим - 2 случай (открываем экран создания заметки или выходим)
            if((slidingPane.isSlideable && slidingPane.isOpen)) {
                if(globalViewModel.hasUnsavedChanges)
                    showUnsavedChangesAlert(requireContext()) { globalViewModel.closeDetails() }
                else globalViewModel.closeDetails()
            } else if(!slidingPane.isSlideable) {
                if(globalViewModel.uiState.value is GlobalUiState.EditNote) {
                    if(globalViewModel.hasUnsavedChanges)
                        showUnsavedChangesAlert(requireContext()) { globalViewModel.openCreateNote()}
                    else globalViewModel.openCreateNote()
                } else {
                    showOnExitAlert(requireContext()) { requireActivity().finish() }
                }
            } else {
                showOnExitAlert(requireContext()) { requireActivity().finish() }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}