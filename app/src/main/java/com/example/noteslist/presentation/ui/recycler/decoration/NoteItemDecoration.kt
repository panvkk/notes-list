package com.example.noteslist.presentation.ui.recycler.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class NoteItemDecoration(
    private val verticalMarginPx: Int,
    private val horizontalMarginPx: Int
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.left = horizontalMarginPx
        outRect.top = verticalMarginPx
        outRect.right = horizontalMarginPx
        outRect.bottom = verticalMarginPx
    }
}