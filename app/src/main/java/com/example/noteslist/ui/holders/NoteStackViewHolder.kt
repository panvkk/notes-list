package com.example.noteslist.ui.holders

import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.noteslist.data.ViewTyped
import com.example.noteslist.databinding.ItemNoteStackViewBinding
import com.example.noteslist.ui.view.NoteView

class NoteStackViewHolder(private val binding: ItemNoteStackViewBinding)
    : RecyclerView.ViewHolder(binding.root) {

    fun bind(noteStack: ViewTyped.NoteStack) {
        binding.noteStack.removeAllViews()

        noteStack.notes.forEach { note ->
            val noteView = NoteView(binding.root.context).apply {
                title = note.title
                description = note.description
                date = note.date
                importance = note.isImportant

                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }

            binding.noteStack.addView(noteView)
        }
    }
}