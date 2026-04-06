package com.example.noteslist.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.noteslist.R
import com.example.noteslist.databinding.FragmentNotesListBinding
import com.example.noteslist.presentation.ui.recycler.adapter.MultiTypeAdapter
import com.example.noteslist.presentation.ui.recycler.adapter.delegates.DateTitleDelegate
import com.example.noteslist.presentation.ui.recycler.adapter.delegates.NoteDelegate
import com.example.noteslist.presentation.ui.recycler.adapter.delegates.NoteStackDelegate
import com.example.noteslist.presentation.ui.recycler.decoration.NoteItemDecoration
import com.example.noteslist.presentation.viewmodel.NotesListViewModel
import kotlinx.coroutines.launch

class NotesListFragment : Fragment() {

    private var _binding: FragmentNotesListBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<NotesListViewModel> { NotesListViewModel.factory }

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
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
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
    }

    private fun setupListeners() {
        binding.addNoteButton.setOnClickListener {
            if(it.alpha > 0f) {
                findNavController().navigate(
                    NotesListFragmentDirections.openDetails(-1L)
                )
            }
        }
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            val animDuration = 200L

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                binding.addNoteButton.apply {
                    clearAnimation()
                    when(newState) {
                        RecyclerView.SCROLL_STATE_IDLE -> {
                            animate().alpha(1f).setDuration(animDuration)
                        }
                        RecyclerView.SCROLL_STATE_DRAGGING -> {
                            animate().alpha(0f).setDuration(animDuration)
                        }
                    }
                }
            }
        })
    }

    private fun setupAdapter() : MultiTypeAdapter {
        val navController = findNavController()

        val onNoteClick = { noteId: Long ->
            navController.navigate(NotesListFragmentDirections.openDetails(noteId)) }
        val onNoteLongClick = { noteId: Long -> viewModel.onNoteLongClick(noteId) }
        val onStackExpandClick = { stackId: Int -> viewModel.expandStack(stackId) }
        val onStackCollapseClick = { stackId: Int -> viewModel.collapseStack(stackId) }
        val isStackExpanded = { stackId: Int -> viewModel.isStackExpanded(stackId)}

        val delegates = listOf(
            NoteDelegate(onNoteClick, onNoteLongClick),
            NoteStackDelegate(
                onNoteClick, onNoteLongClick,
                onStackExpandClick, onStackCollapseClick,
                isStackExpanded
            ),
            DateTitleDelegate()
        )
        val adapter = MultiTypeAdapter(delegates)
        return adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}