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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.fontResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.noteslist.R
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
    val isNewScreen = viewModel.isNewNote.collectAsState().value

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        TextField(
            value = titleInput,
            onValueChange = { viewModel.updateNoteTitle(it) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            maxLines = 1,
            label = { Text(stringResource(R.string.text_field_title_label)) },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedLabelColor = colorResource(R.color.text_field_label_color),
                cursorColor = colorResource(R.color.text_field_cursor_color)
            )
        )
        TextField(
            value = descriptionInput,
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
                checked = isImportant,
                onCheckedChange = { viewModel.updateIsNoteImportant(it) },
                colors = SwitchDefaults.colors(
                    checkedBorderColor = colorResource(R.color.active_button_color),
                    checkedTrackColor =  colorResource(R.color.active_button_color)
                )
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = {
                    viewModel.cancel()
                    onClickBack()},
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
                onClick = {
                    viewModel.createNote()
                    onClickBack()},
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.active_button_color)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(dimensionResource(R.dimen.button_height))
            ) {
                Text(
                    text = stringResource(R.string.create_button_text),
                    fontSize = with(LocalDensity.current) { dimensionResource(R.dimen.button_text_size).toSp() },
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    }
}

@Preview
@Composable
fun NoteDetailsScreenPreview() {
//    Scaffold { innerPadding ->
//        NoteDetailsScreen(
//            modifier = Modifier.padding(innerPadding),
//
//        )
//    }
}