package com.example.noteslist.presentation.ui.recycler.holders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.noteslist.presentation.model.ViewTypedModel
import com.example.noteslist.databinding.ItemNoteViewBinding

class NoteViewHolder(
    private val binding: ItemNoteViewBinding,
    private val onClick: (noteId: Long) -> Unit,
    private val onLongClick: (noteId: Long) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private val onLongClickListener = View.OnLongClickListener { view ->
        val noteId = view.tag as Long
        onLongClick(noteId)
        true
    }

    private val onClickListener = View.OnClickListener { view ->
        val noteId = view.tag as Long
        onClick(noteId)
    }

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