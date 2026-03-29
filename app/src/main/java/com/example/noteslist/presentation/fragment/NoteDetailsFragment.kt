package com.example.noteslist.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.noteslist.databinding.FragmentNoteDetailsBinding
import com.example.noteslist.databinding.FragmentNotesListBinding
import com.example.noteslist.presentation.viewmodel.NoteDetailsViewModel
import kotlin.getValue

class NoteDetailsFragment : Fragment() {

    private var _binding: FragmentNoteDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<NoteDetailsViewModel> { NoteDetailsViewModel.factory }

    private var noteId: Long? = null

    companion object {
        const val ARG_NOTE_ID = "arg_note_id"

        fun newInstance(noteId: Long?) : NoteDetailsFragment {
            return NoteDetailsFragment().apply {
                arguments = Bundle().apply {
                    if(noteId != null) {
                        putLong(ARG_NOTE_ID, noteId)
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        noteId = arguments?.getLong(ARG_NOTE_ID, -1L).takeIf { it != -1L }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}