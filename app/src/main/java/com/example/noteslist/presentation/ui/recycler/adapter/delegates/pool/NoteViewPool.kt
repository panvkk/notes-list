package com.example.noteslist.presentation.ui.recycler.adapter.delegates.pool

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.noteslist.databinding.ItemNoteViewBinding

class NoteViewPool(
    private val onNoteClick: (noteId: Long) -> Unit,
    private val onNoteLongClick: (noteId: Long) -> Unit
) {
    private val pool: MutableList<ItemNoteViewBinding> = mutableListOf()

    private val onNoteClickListener = View.OnClickListener { view ->
        val noteId = view.tag as Long
        onNoteClick(noteId)
    }

    private val onNoteLongClickListener = View.OnLongClickListener { view ->
        val noteId = view.tag as Long
        onNoteLongClick(noteId)
        true
    }

    fun getView(parent: ViewGroup) : ItemNoteViewBinding {
        return if(pool.isNotEmpty()) {
            val noteView = pool.first()
            pool.removeAt(0)
            noteView
        } else {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemNoteViewBinding.inflate(inflater)
            binding.noteView.apply {
                setOnClickListener(onNoteClickListener)
                setOnLongClickListener(onNoteLongClickListener)
            }
            binding
        }
    }

    fun putView(binding: ItemNoteViewBinding) {
        binding.noteView.apply {
            translationZ = 0f
            alpha = 1f
            isRead = false
            visibility = View.VISIBLE
        }
        pool.add(binding)
    }
}