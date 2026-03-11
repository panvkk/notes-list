package com.example.noteslist.ui.holders

import androidx.recyclerview.widget.RecyclerView
import com.example.noteslist.data.ViewTyped
import com.example.noteslist.databinding.ItemNoteViewBinding

class NoteViewHolder(private val binding: ItemNoteViewBinding)
    : RecyclerView.ViewHolder(binding.root) {

    fun bind(note: ViewTyped.Note) {
        binding.noteView.title = note.title
        binding.noteView.description = note.description
        binding.noteView.date = note.date
        binding.noteView.importance = note.isImportant
    }
}