package com.example.noteslist.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.noteslist.databinding.FragmentMainHostBinding
import com.example.noteslist.presentation.model.GlobalUiState
import com.example.noteslist.presentation.viewmodel.GlobalViewModel
import kotlinx.coroutines.launch
import kotlin.getValue

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

        val slidingPane = binding.slidingPaneLayout
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}