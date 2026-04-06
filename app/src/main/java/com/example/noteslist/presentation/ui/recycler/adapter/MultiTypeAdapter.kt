package com.example.noteslist.presentation.ui.recycler.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.example.noteslist.presentation.model.ViewTypedModel
import com.example.noteslist.presentation.ui.recycler.adapter.delegates.AdapterDelegate
import com.example.noteslist.presentation.ui.recycler.holders.NoteStackViewHolder
import com.example.noteslist.presentation.ui.recycler.util.ViewTypedDiffUtilCallback

class MultiTypeAdapter(
    private val delegates: List<AdapterDelegate<ViewTypedModel>>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val differ = AsyncListDiffer(this, ViewTypedDiffUtilCallback())

    private val items: List<ViewTypedModel>
        get() = differ.currentList

    override fun getItemViewType(position: Int): Int {
        for((index, delegate) in delegates.withIndex()) {
            if(delegate.isForViewType(items, position)) {
                return index
            }
        }
        throw IllegalAccessException("No delegate found for position $position")
    }

    private fun getDelegateForPosition(position: Int) : AdapterDelegate<ViewTypedModel> {
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

    fun setNewData(newItems: List<ViewTypedModel>) {
        differ.submitList(newItems)
    }
}