package com.example.noteslist.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.noteslist.R
import com.example.noteslist.databinding.FragmentNotesListBinding
import com.example.noteslist.presentation.model.SettingsUiState
import com.example.noteslist.presentation.ui.recycler.adapter.MultiTypeAdapter
import com.example.noteslist.presentation.ui.recycler.adapter.delegates.DateTitleDelegate
import com.example.noteslist.presentation.ui.recycler.adapter.delegates.NoteDelegate
import com.example.noteslist.presentation.ui.recycler.adapter.delegates.NoteStackDelegate
import com.example.noteslist.presentation.ui.recycler.decoration.NoteItemDecoration
import com.example.noteslist.presentation.viewmodel.GlobalViewModel
import com.example.noteslist.presentation.viewmodel.NotesListViewModel
import kotlinx.coroutines.launch

internal class NotesListFragment : Fragment() {

    private var _binding: FragmentNotesListBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<NotesListViewModel> { NotesListViewModel.factory }
    private val globalViewModel: GlobalViewModel by activityViewModels()

    private val notesAdapter by lazy { setupAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerItemsHorizontalMargin = resources.getDimensionPixelSize(R.dimen.recycler_item_horizontal_margin)
        val recyclerItemsVerticalMargin = resources.getDimensionPixelSize(R.dimen.recycler_item_vertical_margin)

        with(binding.recyclerView) {
            adapter = notesAdapter
            layoutManager = setupLayoutManager()
            clipChildren = false
            clipToPadding = false

            if(itemDecorationCount == 0) {
                addItemDecoration(NoteItemDecoration(
                    recyclerItemsVerticalMargin,
                    recyclerItemsHorizontalMargin
                ))
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { newNotes ->
                    notesAdapter.setNewData(newNotes)
                }
            }
        }
        setupListeners()
        setupSearchEditText()
        setupOnScrollAnimations()
    }

    private fun setupSearchEditText() {
        binding.searchQueryField.doAfterTextChanged { text ->
            viewModel.updateSearchQuery(text.toString())
        }
    }

    private fun setupListeners() {
        binding.addNoteButton.setOnClickListener {
            if(it.alpha > 0f) {
                globalViewModel.openCreateNote()
            }
        }
        binding.settingsButton.setOnClickListener {
            findNavController().navigate(MainHostFragmentDirections.openSettings())
        }
    }

    private fun setupOnScrollAnimations() {
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            val animDuration = 200L

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                when(newState) {
                    RecyclerView.SCROLL_STATE_IDLE -> {
                        binding.addNoteButton.apply {
                            clearAnimation()
                            animate().alpha(1f).setDuration(animDuration)
                        }
                        binding.settingsButton.apply {
                            clearAnimation()
                            animate().alpha(1f).setDuration(animDuration)
                        }
                        binding.searchQueryFieldContainer.apply {
                            clearAnimation()
                            animate().alpha(1f).setDuration(animDuration)
                        }
                    }
                    RecyclerView.SCROLL_STATE_DRAGGING -> {
                        binding.addNoteButton.apply {
                            clearAnimation()
                            animate().alpha(0f).setDuration(animDuration)
                        }
                        binding.settingsButton.apply {
                            clearAnimation()
                            animate().alpha(0f).setDuration(animDuration)
                        }
                        binding.searchQueryFieldContainer.apply {
                            clearAnimation()
                            animate().alpha(0f).setDuration(animDuration)
                        }
                    }
                }
            }
        })
    }

    private fun setupLayoutManager() : RecyclerView.LayoutManager {
        return object : LinearLayoutManager(context) {
            override fun calculateExtraLayoutSpace(state: RecyclerView.State, extraLayoutSpace: IntArray) {
                // Добавляем по 1500 пикселей сверху и снизу для лучшей работы анимаций в ресайклере
                extraLayoutSpace[0] = 1500
                extraLayoutSpace[1] = 1500
            }
        }
    }
    private fun setupAdapter() : MultiTypeAdapter {
        val onNoteClick = { noteId: Long -> globalViewModel.openEditNote(noteId) }
        val onNoteLongClick = { noteId: Long -> viewModel.onNoteLongClick(noteId) }
        val onStackExpandClick = { stackId: Int -> viewModel.expandStack(stackId) }
        val onStackCollapseClick = { stackId: Int -> viewModel.collapseStack(stackId) }
        val isStackExpanded = { stackId: Int -> viewModel.isStackExpanded(stackId)}
        val settings = fetchSettings()

        val delegates = listOf(
            NoteDelegate(onNoteClick, onNoteLongClick),
            NoteStackDelegate(
                onNoteClick, onNoteLongClick,
                onStackExpandClick, onStackCollapseClick,
                isStackExpanded,
                settings
            ),
            DateTitleDelegate()
        )
        val adapter = MultiTypeAdapter(delegates)
        return adapter
    }

    private fun fetchSettings() : SettingsUiState {
        return SettingsUiState(50f, 4)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}