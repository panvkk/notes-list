package com.example.noteslist.ui.recycler.adapters.delegates

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.noteslist.ui.recycler.adapters.MultiTypeAdapter

interface AdapterDelegate<T> {
    fun isForViewType(items: List<T>, position: Int) : Boolean
    fun isForViewType(item: Any) : Boolean

    fun onCreateViewHolder(
        parent: ViewGroup,
        adapter: MultiTypeAdapter? = null,
        viewPool: RecyclerView.RecycledViewPool? = null
    ) : RecyclerView.ViewHolder

    fun onBindViewHolder(
        items: List<T>,
        position: Int,
        holder: RecyclerView.ViewHolder,
    )
}