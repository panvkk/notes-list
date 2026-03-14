package com.example.noteslist.ui.recycler.holders

import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.noteslist.data.ViewTyped
import com.example.noteslist.databinding.ItemNoteStackViewBinding
import com.example.noteslist.ui.view.NoteView
import androidx.core.view.isNotEmpty
import com.example.noteslist.R
import com.example.noteslist.databinding.ItemNoteViewBinding
import com.example.noteslist.ui.recycler.adapters.MultiTypeAdapter

class NoteStackViewHolder(
    private val binding: ItemNoteStackViewBinding,
    private val adapter: MultiTypeAdapter,
    private val viewPool: RecyclerView.RecycledViewPool,
    private val parent: ViewGroup
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(noteStack: ViewTyped.NoteStack) {
        recycleChildren()

        binding.noteStackView.isExpanded = false
        noteStack.notes.forEach { note ->
            val type = adapter.getItemViewTypeForModel(note)

            val vh = (viewPool.getRecycledView(type)
                ?: adapter.createViewHolder(parent, type))
                    as NoteViewHolder

            vh.bind(note)
            vh.itemView.setTag(R.id.noteViewHolderTag, vh)

            binding.noteStackView.addView(vh.itemView)
        }
    }

    fun recycleChildren() {
        binding.noteStackView.apply {
            while(this.isNotEmpty()) {
                val child = this.getChildAt(0)
                val vh = child.getTag(R.id.noteViewHolderTag) as? NoteViewHolder

                this.removeView(child)

                if(vh != null) {
                    child.translationZ = 0f // После NoteStack остаётся дополнительный подъём, который будет лишним в списке
                    setIsRecyclable(true)
                    viewPool.putRecycledView(vh)
                }
            }
        }
    }
}