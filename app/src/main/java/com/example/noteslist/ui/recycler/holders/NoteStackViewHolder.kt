package com.example.noteslist.ui.recycler.holders

import android.os.Build
import android.view.View
import androidx.core.view.ViewCompat.setLayerType
import androidx.recyclerview.widget.RecyclerView
import com.example.noteslist.data.ViewTyped
import com.example.noteslist.databinding.ItemNoteStackViewBinding
import androidx.core.view.isNotEmpty
import com.example.noteslist.R
import com.example.noteslist.databinding.ItemNoteViewBinding
import com.example.noteslist.ui.recycler.adapters.delegates.pool.NoteViewPool

class NoteStackViewHolder(
    private val binding: ItemNoteStackViewBinding,
    private val viewPool: NoteViewPool,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(noteStack: ViewTyped.NoteStack) {
        binding.noteStackView.isExpanded = false
        noteStack.notes.forEach { note ->
            val child = viewPool.getView(binding.noteStackView)

            child.noteView.apply {
                title = note.title
                description = note.description
                date = note.date
                isImportant = note.isImportant
            }
            child.root.setTag(R.id.noteBindingTag, child)
            binding.noteStackView.addView(child.noteView)
        }
    }

    fun recycleChildren() {
        binding.noteStackView.apply {
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