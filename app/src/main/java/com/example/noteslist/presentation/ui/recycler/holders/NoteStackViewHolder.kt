package com.example.noteslist.presentation.ui.recycler.holders

import androidx.core.view.isNotEmpty
import androidx.recyclerview.widget.RecyclerView
import com.example.noteslist.R
import com.example.noteslist.databinding.ItemNoteStackViewBinding
import com.example.noteslist.databinding.ItemNoteViewBinding
import com.example.noteslist.presentation.model.ViewTypedModel
import com.example.noteslist.presentation.ui.recycler.adapter.delegates.pool.NoteViewPool

class NoteStackViewHolder(
    private val binding: ItemNoteStackViewBinding,
    private val viewPool: NoteViewPool
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(noteStack: ViewTypedModel.NoteStack) {
        noteStack.notes.forEach { note ->
            val child = viewPool.getView(binding.noteStackView)

            child.noteView.apply {
                title = note.title
                description = note.description
                date = note.date
                isImportant = note.isImportant
                isRead = note.isRead

                tag = note.id
            }
            child.root.setTag(R.id.noteBindingTag, child)
            binding.noteStackView.addView(child.noteView)
        }
    }

    fun recycleChildren() {
        binding.noteStackView.apply {
            isExpanded = false
            while(this.isNotEmpty()) {
                val child = this.getChildAt(0)

                this.removeView(child)
                val binding = child.getTag(R.id.noteBindingTag) as? ItemNoteViewBinding
                if(binding != null) {
                    binding.noteView.translationZ = 0f
                    viewPool.putView(binding)
                }
            }
        }
    }
}