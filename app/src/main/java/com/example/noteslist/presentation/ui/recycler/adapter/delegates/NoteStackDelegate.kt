package com.example.noteslist.presentation.ui.recycler.adapter.delegates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.noteslist.presentation.model.ViewTyped
import com.example.noteslist.databinding.ItemNoteStackViewBinding
import com.example.noteslist.presentation.ui.recycler.adapter.delegates.pool.NoteViewPool
import com.example.noteslist.presentation.ui.recycler.holders.NoteStackViewHolder

class NoteStackDelegate : AdapterDelegate<ViewTyped> {

    private val stackViewPool = NoteViewPool()

    override fun isForViewType(items: List<ViewTyped>, position: Int): Boolean {
        return items[position] is ViewTyped.NoteStack
    }

    override fun isForViewType(item: Any): Boolean {
        return item is ViewTyped.NoteStack
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemNoteStackViewBinding.inflate(inflater)
        return NoteStackViewHolder(binding, stackViewPool)
    }

    override fun onBindViewHolder(
        items: List<ViewTyped>,
        position: Int,
        holder: RecyclerView.ViewHolder
    ) {
        val noteStack = items[position]
        (holder as? NoteStackViewHolder)?.bind(
            noteStack as ViewTyped.NoteStack
        )
    }
}