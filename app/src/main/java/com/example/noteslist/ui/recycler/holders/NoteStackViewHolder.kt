package com.example.noteslist.ui.recycler.holders

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.noteslist.R
import com.example.noteslist.data.ViewTyped
import com.example.noteslist.databinding.ItemNoteStackViewBinding
import com.example.noteslist.databinding.ItemNoteViewBinding
import com.example.noteslist.ui.view.NoteView

class NoteStackViewHolder(private val binding: ItemNoteStackViewBinding)
    : RecyclerView.ViewHolder(binding.root) {

    fun bind(noteStack: ViewTyped.NoteStack) {
        binding.noteStackView.removeAllViews()

        noteStack.notes.forEach { note ->
            // Вот это очень плохо, при каждом биндинге инфлейтится новая вьюха
            // по-хорошему, для NoteStackViewHolder нужно использовать вьюхолдеры простых NoteView
            // но нету времени на написание ViewPool
            val noteView = LayoutInflater.from(binding.root.context)
                .inflate(R.layout.item_note_view, binding.noteStackView, false)
                    as NoteView

            noteView.apply {
                title = note.title
                description = note.description
                date = note.date
                importance = note.isImportant

                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                setOnClickListener {
                    isRead = !isRead
                }
            }

            binding.noteStackView.addView(noteView)
        }

        binding.noteStackView.apply {
            setOnClickListener {
                if(!isExpanded) isExpanded = true
            }
        }
    }
}