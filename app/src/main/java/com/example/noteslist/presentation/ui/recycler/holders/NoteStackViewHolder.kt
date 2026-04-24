package com.example.noteslist.presentation.ui.recycler.holders

import androidx.recyclerview.widget.RecyclerView
import com.example.noteslist.R
import com.example.noteslist.databinding.ItemNoteStackViewBinding
import com.example.noteslist.databinding.ItemNoteViewBinding
import com.example.noteslist.domain.model.SettingsModel
import com.example.noteslist.presentation.model.ViewTypedModel
import com.example.noteslist.presentation.ui.recycler.adapter.delegates.pool.NoteViewPool
import com.example.noteslist.presentation.ui.view.NoteView

class NoteStackViewHolder(
    private val binding: ItemNoteStackViewBinding,
    private val viewPool: NoteViewPool,
    private val onCollapseClickListener: (Int) -> Unit,
    private val onExpandClickListener: (Int) -> Unit,
    private val isStackExpanded: (Int) -> Boolean,
) : RecyclerView.ViewHolder(binding.root) {

    private var currentStackId = -1

    init {
        binding.noteStackView.apply {
            setOnClickExpandListener { if(currentStackId != -1) onExpandClickListener(currentStackId) }
            setOnClickCollapseListener { if(currentStackId != -1) onCollapseClickListener(currentStackId) }
        }
    }

    fun bindSettings(settings: SettingsModel) {
        binding.noteStackView.apply {
            stackSpacing = settings.stackSpacing
            stackMaxVisible = settings.stackMaxVisible
        }
    }

    fun bind(noteStack: ViewTypedModel.NoteStack) {
        if(binding.noteStackView.childCount > 1)
            recycleChildren()
        currentStackId = noteStack.stackId
        binding.noteStackView.isExpanded = isStackExpanded(noteStack.stackId)
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
            val childrenToRemove = (0 until childCount)
                .map { getChildAt(it) }
                .filterIsInstance<NoteView>()

            childrenToRemove.forEach { child ->
                removeView(child)
                val binding = child.getTag(R.id.noteBindingTag) as? ItemNoteViewBinding
                binding?.let { viewPool.putView(it) }
            }
        }
    }
}