package com.example.noteslist.presentation.ui.recycler.holders

import androidx.recyclerview.widget.RecyclerView
import com.example.noteslist.presentation.model.ViewTypedModel
import com.example.noteslist.databinding.ItemDateTitleBinding

class DateTitleViewHolder(
    private val binding: ItemDateTitleBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(dateTitle: ViewTypedModel.DateTitle) {
        binding.dateTitleText.text = dateTitle.date
    }
}