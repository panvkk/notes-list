package com.example.noteslist.presentation.ui.recycler.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.noteslist.presentation.model.ViewTyped
import com.example.noteslist.presentation.ui.recycler.adapters.delegates.AdapterDelegate
import com.example.noteslist.presentation.ui.recycler.holders.NoteStackViewHolder

class MultiTypeAdapter(
    private val delegates: List<AdapterDelegate<ViewTyped>>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items: List<ViewTyped> = emptyList()

    override fun getItemViewType(position: Int): Int {
        for((index, delegate) in delegates.withIndex()) {
            if(delegate.isForViewType(items, position)) {
                return index
            }
        }
        throw IllegalAccessException("No delegate found for position $position")
    }

    private fun getDelegateForPosition(position: Int) : AdapterDelegate<ViewTyped> {
        val viewTyped = getItemViewType(position)
        return delegates[viewTyped]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return delegates[viewType].onCreateViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getDelegateForPosition(position).onBindViewHolder(items, position, holder)
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        if(holder is NoteStackViewHolder) {
            holder.recycleChildren()
        }
        super.onViewRecycled(holder)
    }

    override fun getItemCount() = items.size

    fun setNewData(newItems: List<ViewTyped>) {
        items = newItems
        notifyDataSetChanged()
    }
}