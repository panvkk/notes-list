package com.example.noteslist.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.noteslist.R
import com.example.noteslist.data.NotesRepository
import com.example.noteslist.data.ViewTyped
import com.example.noteslist.databinding.ActivityMainBinding
import com.example.noteslist.ui.recycler.NoteItemDecoration
import com.example.noteslist.ui.recycler.adapters.MultiTypeAdapter
import com.example.noteslist.ui.recycler.adapters.delegates.DateTitleDelegate
import com.example.noteslist.ui.recycler.adapters.delegates.NoteDelegate
import com.example.noteslist.ui.recycler.adapters.delegates.NoteStackDelegate

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val mainViewModel = MainViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val viewTypedData = mainViewModel.getViewTypedData()
        val recyclerItemsHorizontalMargin = resources.getDimensionPixelSize(R.dimen.recycler_item_horizontal_margin)
        val recyclerItemsVerticalMargin = resources.getDimensionPixelSize(R.dimen.recycler_item_vertical_margin)

        with(binding.recyclerView) {
            adapter = setupAdapter(viewTypedData)
            layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            clipChildren = false
            clipToPadding = false

            addItemDecoration(NoteItemDecoration(
                recyclerItemsVerticalMargin,
                recyclerItemsHorizontalMargin
            ))
        }
    }

    private fun setupAdapter(data: List<ViewTyped>) : MultiTypeAdapter {
        val delegates = listOf(NoteDelegate(), NoteStackDelegate(), DateTitleDelegate())
        val adapter = MultiTypeAdapter(delegates)
        adapter.setNewData(data)
        return adapter
    }
}