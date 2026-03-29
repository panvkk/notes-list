package com.example.noteslist.presentation.ui.recycler.adapter.delegates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.noteslist.presentation.model.ViewTyped
import com.example.noteslist.databinding.ItemNoteViewBinding
import com.example.noteslist.presentation.ui.recycler.holders.NoteViewHolder

class NoteDelegate : AdapterDelegate<ViewTyped> {
    override fun isForViewType(items: List<ViewTyped>, position: Int): Boolean {
        return items[position] is ViewTyped.Note
    }

    override fun isForViewType(item: Any): Boolean {
        return item is ViewTyped.Note
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        // parent для обработки margins, которые указаны в xml
        val binding = ItemNoteViewBinding.inflate(inflater, parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(
        items: List<ViewTyped>,
        position: Int,
        holder: RecyclerView.ViewHolder,
    ) {
        val note = items[position]
        (holder as? NoteViewHolder)?.bind(note as ViewTyped.Note)
    }
}