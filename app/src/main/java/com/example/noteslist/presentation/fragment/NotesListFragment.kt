package com.example.noteslist.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
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

class NotesListFragment : Fragment() {

    private var _binding: FragmentNotesListBinding? = null
    private val binding get() = _binding!!

    // TODO: Убрать эту дрисню
    private val mainViewModel = MainViewModel()

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

        val viewTypedData = mainViewModel.getViewTypedData()
        val recyclerItemsHorizontalMargin = resources.getDimensionPixelSize(R.dimen.recycler_item_horizontal_margin)
        val recyclerItemsVerticalMargin = resources.getDimensionPixelSize(R.dimen.recycler_item_vertical_margin)

        with(binding.recyclerView) {
            adapter = setupAdapter(viewTypedData)
            layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            clipChildren = false
            clipToPadding = false

            addItemDecoration(NoteItemDecoration(
                recyclerItemsVerticalMargin,
                recyclerItemsHorizontalMargin
            ))
        }
    }

    private fun setUpListeners() {
        binding.addNoteButton.setOnClickListener {
//            findNavController().navigate(
//                NotesListFragmentDirections
//            )
        }
    }

    private fun setupAdapter(data: List<ViewTyped>) : MultiTypeAdapter {
        val delegates = listOf(NoteDelegate(), NoteStackDelegate(), DateTitleDelegate())
        val adapter = MultiTypeAdapter(delegates)
        adapter.setNewData(data)
        return adapter
    }
}