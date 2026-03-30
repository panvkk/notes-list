package com.example.noteslist.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.noteslist.R
import com.example.noteslist.databinding.FragmentNotesListBinding
import com.example.noteslist.presentation.model.ViewTyped
import com.example.noteslist.presentation.ui.recycler.adapter.MultiTypeAdapter
import com.example.noteslist.presentation.ui.recycler.adapter.delegates.DateTitleDelegate
import com.example.noteslist.presentation.ui.recycler.adapter.delegates.NoteDelegate
import com.example.noteslist.presentation.ui.recycler.adapter.delegates.NoteStackDelegate
import com.example.noteslist.presentation.ui.recycler.decoration.NoteItemDecoration
import com.example.noteslist.presentation.viewmodel.MainViewModel
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class NotesListFragment : Fragment() {

    private var _binding: FragmentNotesListBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<MainViewModel> { MainViewModel.factory }

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
            layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            clipChildren = false
            clipToPadding = false

            if(itemDecorationCount == 0) {
                addItemDecoration(NoteItemDecoration(
                    recyclerItemsVerticalMargin,
                    recyclerItemsHorizontalMargin
                ))
            }
        }

        viewModel.state
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { notes -> notesAdapter.setNewData(notes) }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        setupListeners()
    }

    private fun setupListeners() {
        binding.addNoteButton.setOnClickListener {
            findNavController().navigate(
                NotesListFragmentDirections.openDetails()
            )
        }
    }

    private fun setupAdapter() : MultiTypeAdapter {
        val delegates = listOf(NoteDelegate(), NoteStackDelegate(), DateTitleDelegate())
        val adapter = MultiTypeAdapter(delegates)
        return adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}