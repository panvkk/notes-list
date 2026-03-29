package com.example.noteslist.presentation.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.noteslist.presentation.viewmodel.CreateNoteViewModel

@Composable
fun NoteDetailsScreen(
    modifier: Modifier = Modifier,
    viewModel: CreateNoteViewModel
) {
    val titleInput = viewModel.titleInput.collectAsState().value
    val descriptionInput = viewModel.descriptionInput.collectAsState().value
    val isImportant = viewModel.isImportant.collectAsState().value


    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        TextField(
            value = titleInput,
            onValueChange = { viewModel.updateTitle(it) },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = descriptionInput,
            onValueChange = { viewModel.updateDescription(it) },
            modifier = Modifier.fillMaxWidth()
        )
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Важно")
            Switch(
                checked = isImportant,
                onCheckedChange = { viewModel.updateIsImportant(it) }
            )
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(onClick = { viewModel.cancel() }) {
                Text("Отмена")
            }
            Button(onClick = { viewModel.createNote() }) {
                Text("Создать")
            }
        }
    }
}

@Preview
@Composable
fun NoteDetailsScreenPreview() {
    Scaffold { innerPadding ->
        NoteDetailsScreen(
            modifier = Modifier.padding(innerPadding),
            CreateNoteViewModel()
        )
    }
}