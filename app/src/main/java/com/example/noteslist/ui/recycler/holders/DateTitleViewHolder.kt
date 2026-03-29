package com.example.noteslist.ui.recycler.holders

import androidx.recyclerview.widget.RecyclerView
import com.example.noteslist.data.ViewTyped
import com.example.noteslist.databinding.ItemDateTitleBinding

class DateTitleViewHolder(
    private val binding: ItemDateTitleBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(dateTitle: ViewTyped.DateTitle) {
        binding.dateTitleText.text = dateTitle.date
    }
}