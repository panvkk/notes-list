package com.example.noteslist.ui.adapters.delegates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.noteslist.data.ViewTyped
import com.example.noteslist.databinding.ItemNoteStackViewBinding
import com.example.noteslist.databinding.ItemNoteViewBinding
import com.example.noteslist.ui.holders.NoteStackViewHolder
import com.example.noteslist.ui.holders.NoteViewHolder

class NoteStackDelegate : AdapterDelegate<ViewTyped> {
    override fun isForViewType(items: List<ViewTyped>, position: Int): Boolean {
        return items[position] is ViewTyped.NoteStack
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemNoteStackViewBinding.inflate(inflater)
        return NoteStackViewHolder(binding)
    }

    override fun onBindViewHolder(
        items: List<ViewTyped>,
        position: Int,
        holder: RecyclerView.ViewHolder
    ) {
        val note = items[position]
        (holder as? NoteStackViewHolder)?.bind(note as ViewTyped.NoteStack)
    }
}