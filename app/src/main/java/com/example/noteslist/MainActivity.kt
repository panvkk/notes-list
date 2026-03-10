package com.example.noteslist

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.children
import com.example.noteslist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val note1 = findViewById<NoteView>(R.id.note_1)
        note1.setOnClickListener { note1.isRead = !note1.isRead }

        val noteStack1 = findViewById<NoteStackView>(R.id.note_stack_1)
        noteStack1.setOnClickListener {
            val isExpanded = noteStack1.isExpanded
            if(!isExpanded) {
                noteStack1.isExpanded = true
            }
        }
        val isNoteStack1Expanded = noteStack1.isExpanded
        if(isNoteStack1Expanded) {
            for (noteView in noteStack1.children.filterIsInstance<NoteView>()) {
                noteView.setOnClickListener { noteView.isRead = !noteView.isRead }
            }
        }
    }
}