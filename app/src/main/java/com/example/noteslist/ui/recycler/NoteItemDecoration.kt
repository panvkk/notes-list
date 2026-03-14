package com.example.noteslist.ui.recycler

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class NoteItemDecoration(private val marginPx: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.left = marginPx
        outRect.top = marginPx
        outRect.right = marginPx
        outRect.bottom = marginPx
    }
}