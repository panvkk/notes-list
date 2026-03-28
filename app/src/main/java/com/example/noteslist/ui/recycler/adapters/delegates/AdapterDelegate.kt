package com.example.noteslist.ui.recycler.adapters.delegates

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

interface AdapterDelegate<T> {
    fun isForViewType(items: List<T>, position: Int) : Boolean

    fun onCreateViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder

    fun onBindViewHolder(items: List<T>, position: Int, holder: RecyclerView.ViewHolder)
}