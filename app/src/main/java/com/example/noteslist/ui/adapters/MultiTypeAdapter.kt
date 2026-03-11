package com.example.noteslist.ui.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.noteslist.data.ViewTyped
import com.example.noteslist.ui.adapters.delegates.AdapterDelegate

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

    override fun getItemCount() = items.size

    fun setNewData(newItems: List<ViewTyped>) {
        items = newItems
        notifyDataSetChanged()
    }

}