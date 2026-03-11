package com.example.noteslist.ui.recycler.adapters.delegates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.noteslist.data.ViewTyped
import com.example.noteslist.databinding.ItemNoteViewBinding
import com.example.noteslist.ui.recycler.holders.NoteViewHolder

class NoteDelegate : AdapterDelegate<ViewTyped> {
    override fun isForViewType(items: List<ViewTyped>, position: Int): Boolean {
        return items[position] is ViewTyped.Note
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemNoteViewBinding.inflate(inflater, parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(
        items: List<ViewTyped>,
        position: Int,
        holder: RecyclerView.ViewHolder
    ) {
        val note = items[position]
        (holder as? NoteViewHolder)?.bind(note as ViewTyped.Note)
    }
}