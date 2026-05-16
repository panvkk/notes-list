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
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.noteslist.R
import com.example.noteslist.presentation.ui.component.SettingValueCard
import com.example.noteslist.presentation.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onClickCancel: () -> Unit,
    onClickSave: () -> Unit
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle().value

    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.large_padding)),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(dimensionResource(R.dimen.large_padding))
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = stringResource(R.string.settings_title),
            style = MaterialTheme.typography.headlineMedium
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.large_padding)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.stack_spacing_parameter),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f)
                )
                Slider(
                    value = state.stackSpacing,
                    onValueChange = { viewModel.updateStackSpacing(it) },
                    valueRange = 1f..100f,
                    modifier = Modifier.weight(1f)
                )
            }
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.stack_max_visible_parameter),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f)
                )
                SettingValueCard(
                    value = state.stackMaxVisible,
                    onDecreaseValue = { viewModel.decreaseStackMaxVisible() },
                    onIncreaseValue = { viewModel.increaseStackMaxVisible() },
                    modifier = Modifier.weight(1f)
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.medium_padding))
            ) {
                OutlinedButton(
                    onClick = { onClickCancel() },
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
                    onClick = { onClickSave() },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = MaterialTheme.shapes.large,
                    modifier = Modifier
                        .weight(1f)
                        .height(dimensionResource(R.dimen.button_height))
                ) {
                    Text(
                        text = stringResource(R.string.update_button_text),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }
    }
}