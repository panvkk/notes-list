package com.example.noteslist.presentation.ui.recycler.util

import androidx.recyclerview.widget.DiffUtil
import com.example.noteslist.presentation.model.ViewTypedModel

class ViewTypedDiffUtilCallback : DiffUtil.ItemCallback<ViewTypedModel>() {
    override fun areItemsTheSame(oldItem: ViewTypedModel, newItem: ViewTypedModel): Boolean {
        return oldItem.uid == newItem.uid
    }

    override fun areContentsTheSame(oldItem: ViewTypedModel, newItem: ViewTypedModel): Boolean {
        return oldItem == newItem
    }
}