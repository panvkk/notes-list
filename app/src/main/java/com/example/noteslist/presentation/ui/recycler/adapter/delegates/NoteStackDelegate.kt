package com.example.noteslist.presentation.ui.recycler.adapter.delegates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.noteslist.presentation.model.ViewTypedModel
import com.example.noteslist.databinding.ItemNoteStackViewBinding
import com.example.noteslist.presentation.ui.recycler.adapter.delegates.pool.NoteViewPool
import com.example.noteslist.presentation.ui.recycler.holders.NoteStackViewHolder

class NoteStackDelegate(
    private val onNoteClick: (Long) -> Unit,
    private val onNoteLongClick: (Long) -> Unit
) : AdapterDelegate<ViewTypedModel> {

    private val stackViewPool = NoteViewPool(onNoteClick, onNoteLongClick)

    override fun isForViewType(items: List<ViewTypedModel>, position: Int): Boolean {
        return items[position] is ViewTypedModel.NoteStack
    }

    override fun isForViewType(item: Any): Boolean {
        return item is ViewTypedModel.NoteStack
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemNoteStackViewBinding.inflate(inflater)
        return NoteStackViewHolder(binding, stackViewPool)
    }

    override fun onBindViewHolder(
        items: List<ViewTypedModel>,
        position: Int,
        holder: RecyclerView.ViewHolder
    ) {
        val noteStack = items[position]
        (holder as? NoteStackViewHolder)?.bind(
            noteStack as ViewTypedModel.NoteStack
        )
    }
}