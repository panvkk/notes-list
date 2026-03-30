package com.example.noteslist.presentation.ui.screen

import android.text.Layout
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.noteslist.domain.usecase.CreateNoteUseCase
import com.example.noteslist.presentation.viewmodel.NoteDetailsViewModel

@Composable
fun NoteDetailsScreen(
    viewModel: NoteDetailsViewModel,
    onClickBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val titleInput = viewModel.uiState.collectAsState().value.noteTitle
    val descriptionInput = viewModel.uiState.collectAsState().value.noteDescription
    val isImportant = viewModel.uiState.collectAsState().value.isNoteImportant


    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        TextField(
            value = titleInput,
            onValueChange = { viewModel.updateNoteTitle(it) },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = descriptionInput,
            onValueChange = { viewModel.updateNoteDescription(it) },
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Важно",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(end = 8.dp)
                    .align(Alignment.CenterVertically)
            )
            Switch(
                checked = isImportant,
                onCheckedChange = { viewModel.updateIsNoteImportant(it) }
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = {
                    viewModel.cancel()
                    onClickBack()},
                modifier = Modifier.weight(1f)
            ) {
                Text("Отмена")
            }
            Button(
                onClick = {
                    viewModel.createNote()
                    onClickBack()},
                modifier = Modifier.weight(1f)
            ) {
                Text("Создать")
            }
        }
    }
}

@Preview
@Composable
fun NoteDetailsScreenPreview() {
    Scaffold { innerPadding ->
//        NoteDetailsScreen(
//            modifier = Modifier.padding(innerPadding),
//
//        )
    }
}