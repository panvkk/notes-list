package com.example.noteslist.ui.recycler.adapters.delegates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.noteslist.R
import com.example.noteslist.data.ViewTyped
import com.example.noteslist.databinding.ItemNoteStackViewBinding
import com.example.noteslist.ui.recycler.adapters.MultiTypeAdapter
import com.example.noteslist.ui.recycler.holders.NoteStackViewHolder

class NoteStackDelegate : AdapterDelegate<ViewTyped> {
    override fun isForViewType(items: List<ViewTyped>, position: Int): Boolean {
        return items[position] is ViewTyped.NoteStack
    }

    override fun isForViewType(item: Any): Boolean {
        return item is ViewTyped.NoteStack
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        adapter: MultiTypeAdapter?,
        viewPool: RecyclerView.RecycledViewPool?
    ): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemNoteStackViewBinding.inflate(inflater)
        val vh = NoteStackViewHolder(
            binding,
            adapter ?: throw Throwable("NoteStack can't have null adapter"),
            viewPool ?: throw Throwable("NoteStack can't have null viewPool"),
            parent
        )
        vh.itemView.setTag(R.id.noteViewHolderTag, vh)
        return vh
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