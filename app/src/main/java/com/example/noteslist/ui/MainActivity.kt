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
import com.example.noteslist.ui.recycler.adapters.MultiTypeAdapter
import com.example.noteslist.ui.recycler.adapters.delegates.NoteDelegate
import com.example.noteslist.ui.recycler.adapters.delegates.NoteStackDelegate

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

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

        val notes = getData()
        val viewTypedData = getViewTypedData(notes)
        with(binding.recyclerView) {
            adapter = setupAdapter(viewTypedData)
            layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        }
    }

    private fun setupAdapter(data: List<ViewTyped>) : MultiTypeAdapter {
        val delegates = listOf(NoteDelegate(), NoteStackDelegate())
        val adapter = MultiTypeAdapter(delegates)
        adapter.setNewData(data)
        return adapter
    }


    private fun getViewTypedData(notes: List<ViewTyped.Note>) : List<ViewTyped> {
        val viewTypedData = mutableListOf<ViewTyped>()
        val notImportantNotes = mutableListOf<ViewTyped.Note>()
        notes.forEach { note ->
            if(note.isImportant) {
                if(notImportantNotes.isNotEmpty()) {
                    val noteStackChildren = notImportantNotes.toList()
                    val noteStack = ViewTyped.NoteStack(noteStackChildren)
                    viewTypedData.add(noteStack)
                    notImportantNotes.clear()
                }
                viewTypedData.add(note)
            } else {
                notImportantNotes.add(note)
            }
        }
        if(notImportantNotes.isNotEmpty())
            viewTypedData.add(ViewTyped.NoteStack(notImportantNotes))

        return viewTypedData
    }

    private fun getData() : List<ViewTyped.Note> {
        val repository = NotesRepository()
        return repository.getNotes()
    }
}