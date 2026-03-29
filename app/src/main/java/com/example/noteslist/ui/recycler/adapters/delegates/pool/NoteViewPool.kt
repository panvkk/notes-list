package com.example.noteslist.ui.recycler.adapters.delegates.pool

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.noteslist.databinding.ItemNoteViewBinding

class NoteViewPool {
    private val pool: MutableList<ItemNoteViewBinding> = mutableListOf()

    fun getView(parent: ViewGroup) : ItemNoteViewBinding {
        return if(pool.isNotEmpty()) {
            val noteView = pool.first()
            pool.removeFirst()
            noteView
        } else {
            val inflater = LayoutInflater.from(parent.context)
            ItemNoteViewBinding.inflate(inflater)
        }
    }

    fun putView(binding: ItemNoteViewBinding) {
        binding.noteView.apply {
            translationZ = 0f
            alpha = 1f
            isRead = false
        }
        pool.add(binding)
    }
}