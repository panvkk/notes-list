package com.example.noteslist.presentation.ui.recycler.adapters.delegates

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

interface AdapterDelegate<T> {
    fun isForViewType(items: List<T>, position: Int) : Boolean
    fun isForViewType(item: Any) : Boolean

    fun onCreateViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder

    fun onBindViewHolder(
        items: List<T>,
        position: Int,
        holder: RecyclerView.ViewHolder,
    )
}