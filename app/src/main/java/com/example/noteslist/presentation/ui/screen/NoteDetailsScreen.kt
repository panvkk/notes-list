package com.example.noteslist.presentation.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.noteslist.R
import com.example.noteslist.presentation.model.DetailsScreenError
import com.example.noteslist.presentation.ui.component.CustomTextField
import com.example.noteslist.presentation.viewmodel.NavigationEvent
import com.example.noteslist.presentation.viewmodel.NoteDetailsViewModel

@Composable
fun NoteDetailsScreen(
    viewModel: NoteDetailsViewModel,
    onClickSave: () -> Unit,
    onClickCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentNote = viewModel.uiState.collectAsStateWithLifecycle().value.currentNote
    val currentError = viewModel.uiState.collectAsStateWithLifecycle().value.error
    val isNewNote = viewModel.isNewNote.collectAsStateWithLifecycle().value

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when(event) {
                NavigationEvent.OnCancel -> onClickCancel()
                NavigationEvent.OnSave -> onClickSave()
            }
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.medium_padding)),
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .padding(dimensionResource(R.dimen.large_padding))
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        CustomTextField(
            value = currentNote.title,
            onValueChange = { viewModel.updateNoteTitle(it) },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1,
            label = { Text(stringResource(R.string.text_field_title_label)) },
            isError = currentError is DetailsScreenError.HasTitle
        )
        if(currentError is DetailsScreenError.HasTitle) {
            Text(
                text = if(currentError is DetailsScreenError.HasTitle.Empty) stringResource(R.string.empty_title_error)
                    else stringResource(R.string.title_large_error),
                style = MaterialTheme.typography.titleSmall,
                color = colorResource(R.color.error_color),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(start = dimensionResource(R.dimen.extra_small_padding))
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
                    .padding(horizontal = dimensionResource(R.dimen.small_padding))
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
                        .padding(horizontal = dimensionResource(R.dimen.small_padding))
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
                modifier = Modifier.padding(dimensionResource(R.dimen.small_padding))
            )
        }
        if(currentError is DetailsScreenError.NoDiscSpace) {
            Text(
                text = stringResource(R.string.no_disc_space_error),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(dimensionResource(R.dimen.small_padding))
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.medium_padding))
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