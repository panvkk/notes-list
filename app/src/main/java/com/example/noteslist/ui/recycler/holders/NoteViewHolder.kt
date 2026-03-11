package com.example.noteslist.ui.recycler.holders

import androidx.recyclerview.widget.RecyclerView
import com.example.noteslist.data.ViewTyped
import com.example.noteslist.databinding.ItemNoteViewBinding

class NoteViewHolder(private val binding: ItemNoteViewBinding)
    : RecyclerView.ViewHolder(binding.root) {

    fun bind(note: ViewTyped.Note) {
        binding.noteView.apply {
            title = note.title
            description = note.description
            date = note.date
            importance = note.isImportant

            setOnClickListener {
                isRead = !isRead
            }
        }
    }
}