package com.example.noteslist.presentation.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.noteslist.R
import com.example.noteslist.presentation.model.DetailsScreenError
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
        TextField(
            value = currentNote.title,
            onValueChange = { viewModel.updateNoteTitle(it) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            maxLines = 1,
            label = { Text(stringResource(R.string.text_field_title_label)) },
            isError = currentError is DetailsScreenError.TitleEmpty,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedLabelColor = colorResource(R.color.text_field_label_color),
                cursorColor = colorResource(R.color.text_field_cursor_color),
                errorTextColor = colorResource(R.color.error_color),
                errorContainerColor = colorResource(R.color.error_light_color),
            )
        )
        if(currentError is DetailsScreenError.TitleEmpty) {
            Text(
                text = currentError.message,
                fontSize = with(LocalDensity.current) { dimensionResource(R.dimen.title_error_text_size).toSp() },
                color = colorResource(R.color.error_color),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
        TextField(
            value = currentNote.description,
            onValueChange = { viewModel.updateNoteDescription(it) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            label = { Text(stringResource(R.string.text_field_description_label)) },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedLabelColor = colorResource(R.color.text_field_label_color),
                cursorColor = colorResource(R.color.text_field_cursor_color)
            )
        )
        Row {
            Text(
                text = stringResource(R.string.importance_switch_title_text),
                fontSize = with(LocalDensity.current) { dimensionResource(R.dimen.switch_title_text_size).toSp() },
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .align(Alignment.CenterVertically)
            )
            Switch(
                checked = currentNote.isImportant,
                onCheckedChange = { viewModel.updateIsNoteImportant(it) },
                colors = SwitchDefaults.colors(
                    checkedBorderColor = colorResource(R.color.active_button_color),
                    checkedTrackColor =  colorResource(R.color.active_button_color)
                )
            )
        }
        if(!isNewNote) {
            Row {
                Text(
                    text = stringResource(R.string.is_read_switch_title_text),
                    fontSize = with(LocalDensity.current) { dimensionResource(R.dimen.switch_title_text_size).toSp() },
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .align(Alignment.CenterVertically)
                )
                Switch(
                    checked = currentNote.isRead,
                    onCheckedChange = { viewModel.updateIsNoteRead(it) },
                    colors = SwitchDefaults.colors(
                        checkedBorderColor = colorResource(R.color.active_button_color),
                        checkedTrackColor =  colorResource(R.color.active_button_color)
                    )
                )
            }
            Text(
                text = stringResource(R.string.creation_date_prefix_text) + currentNote.date,
                fontSize = with(LocalDensity.current) { dimensionResource(R.dimen.date_title_text_size).toSp() },
                color = colorResource(R.color.note_date),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(8.dp)
            )
        }
        if(currentError is DetailsScreenError.Other) {
            Text(
                text = currentError.message,
                fontSize = with(LocalDensity.current) { dimensionResource(R.dimen.error_title_text_size).toSp() },
                color = colorResource(R.color.error_color),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(8.dp)
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = { viewModel.cancel() },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(dimensionResource(R.dimen.button_height))
            ) {
                Text(
                    text = stringResource(R.string.cancel_button_text),
                    fontSize = with(LocalDensity.current) { dimensionResource(R.dimen.button_text_size).toSp() },
                    fontWeight = FontWeight.Bold
                )
            }
            Button(
                onClick = { viewModel.submitNote() },
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.active_button_color)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(dimensionResource(R.dimen.button_height))
            ) {
                Text(
                    text = if(isNewNote) stringResource(R.string.create_button_text)
                        else stringResource(R.string.update_button_text),
                    fontSize = with(LocalDensity.current) { dimensionResource(R.dimen.button_text_size).toSp() },
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    }
}