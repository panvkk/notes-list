package com.example.noteslist.presentation.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.noteslist.R
import com.example.noteslist.presentation.model.DetailsScreenError
import com.example.noteslist.presentation.ui.component.CustomTextField
import com.example.noteslist.presentation.viewmodel.NavigationEvent
import com.example.noteslist.presentation.viewmodel.NoteDetailsViewModel

@Composable
fun NoteDetailsScreen(
    viewModel: NoteDetailsViewModel,
    onClickBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentNote = viewModel.uiState.collectAsState().value.note
    val currentError = viewModel.uiState.collectAsState().value.error
    val isNewNote = viewModel.isNewNote.collectAsState().value

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when(event) {
                NavigationEvent.NavigateBack -> onClickBack()
            }
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        CustomTextField(
            value = currentNote.title,
            onValueChange = { viewModel.updateNoteTitle(it) },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1,
            label = { Text(stringResource(R.string.text_field_title_label)) },
            isError = currentError is DetailsScreenError.TitleEmpty
        )
        if(currentError is DetailsScreenError.TitleEmpty) {
            Text(
                text = stringResource(R.string.empty_title_error),
                style = MaterialTheme.typography.titleSmall,
                color = colorResource(R.color.error_color),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
        CustomTextField(
            value = currentNote.description,
            onValueChange = { viewModel.updateNoteDescription(it) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.text_field_description_label)) }
        )
        Row {
            Text(
                text = stringResource(R.string.importance_switch_title_text),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .align(Alignment.CenterVertically)
            )
            Switch(
                checked = currentNote.isImportant,
                onCheckedChange = { viewModel.updateIsNoteImportant(it) },
                colors = SwitchDefaults.colors(
                    checkedBorderColor = MaterialTheme.colorScheme.primary,
                    checkedTrackColor =  MaterialTheme.colorScheme.primary
                )
            )
        }
        if(!isNewNote) {
            Row {
                Text(
                    text = stringResource(R.string.is_read_switch_title_text),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .align(Alignment.CenterVertically)
                )
                Switch(
                    checked = currentNote.isRead,
                    onCheckedChange = { viewModel.updateIsNoteRead(it) },
                    colors = SwitchDefaults.colors(
                        checkedBorderColor = MaterialTheme.colorScheme.primary,
                        checkedTrackColor =  MaterialTheme.colorScheme.primary
                    )
                )
            }
            Text(
                text = stringResource(R.string.creation_date_prefix_text) + currentNote.date,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(8.dp)
            )
        }
        if(currentError is DetailsScreenError.Other) {
            Text(
                text = currentError.message ?: stringResource(R.string.unknown_error),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(8.dp)
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = { viewModel.cancel() },
                shape = MaterialTheme.shapes.large,
                modifier = Modifier
                    .weight(1f)
                    .height(dimensionResource(R.dimen.button_height))
            ) {
                Text(
                    text = stringResource(R.string.cancel_button_text),
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Button(
                onClick = { viewModel.submitNote() },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = MaterialTheme.shapes.large,
                modifier = Modifier
                    .weight(1f)
                    .height(dimensionResource(R.dimen.button_height))
            ) {
                Text(
                    text = if(isNewNote) stringResource(R.string.create_button_text)
                        else stringResource(R.string.update_button_text),
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}