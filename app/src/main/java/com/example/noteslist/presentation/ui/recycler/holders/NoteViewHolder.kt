package com.example.noteslist.presentation.ui.recycler.holders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.noteslist.presentation.model.ViewTypedModel
import com.example.noteslist.databinding.ItemNoteViewBinding

class NoteViewHolder(
    private val binding: ItemNoteViewBinding,
    private val onClickListener: View.OnClickListener,
    private val onLongClickListener: View.OnLongClickListener
) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.noteView.apply {
            setOnClickListener(onClickListener)
            setOnLongClickListener(onLongClickListener)
        }
    }

    fun bind(note: ViewTypedModel.Note) {
        binding.noteView.apply {
            title = note.title
            description = note.description
            date = note.date
            isImportant = note.isImportant
            isRead = note.isRead

            tag = note.id
        }
    }
}