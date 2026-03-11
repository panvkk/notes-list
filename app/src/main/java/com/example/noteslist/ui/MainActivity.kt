package com.example.noteslist.ui

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.children
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.noteslist.R
import com.example.noteslist.data.Note
import com.example.noteslist.data.NotesRepository
import com.example.noteslist.data.ViewTyped
import com.example.noteslist.databinding.ActivityMainBinding
import com.example.noteslist.ui.adapters.MultiTypeAdapter
import com.example.noteslist.ui.adapters.delegates.AdapterDelegate
import com.example.noteslist.ui.adapters.delegates.NoteDelegate
import com.example.noteslist.ui.adapters.delegates.NoteStackDelegate
import com.example.noteslist.ui.view.NoteStackView
import com.example.noteslist.ui.view.NoteView

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
//        val note1 = findViewById<NoteView>(R.id.note_1)
//        note1.setOnClickListener { note1.isRead = !note1.isRead }
//
//        val noteStack1 = findViewById<NoteStackView>(R.id.note_stack_1)
//        noteStack1.setOnClickListener {
//            val isExpanded = noteStack1.isExpanded
//            if(!isExpanded) {
//                noteStack1.isExpanded = true
//            }
//        }
//        val isNoteStack1Expanded = noteStack1.isExpanded
//        for (noteView in noteStack1.children.filterIsInstance<NoteView>()) {
//            noteView.setOnClickListener { noteView.isRead = !noteView.isRead }
//        }
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
                    viewTypedData.add(ViewTyped.NoteStack(notImportantNotes))
                    notImportantNotes.clear()
                }
                viewTypedData.add(note)
            } else {
                notImportantNotes.add(note)
            }
        }
        return viewTypedData
    }

    private fun getData() : List<ViewTyped.Note> {
        val repository = NotesRepository()
        return repository.getNotes()
    }
}