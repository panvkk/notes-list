package com.example.noteslist.presentation.ui.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.noteslist.R

@Composable
fun SettingValueCard(
    value: Int,
    onIncreaseValue: () -> Unit,
    onDecreaseValue: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.medium_padding)),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        IconButton(
            onClick = { onDecreaseValue() },
            modifier = Modifier
                .size(dimensionResource(R.dimen.icon_container_size))
                .clip(MaterialTheme.shapes.medium)
                .border(dimensionResource(R.dimen.border_width), Color.Gray, MaterialTheme.shapes.medium)
        ) {
            Icon(
                painter = painterResource(R.drawable.decrease_icon),
                contentDescription = stringResource(R.string.decrease_icon_description),
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(dimensionResource(R.dimen.icon_size))
            )
        }
        Text(
            text = "$value",
            style = MaterialTheme.typography.bodyMedium
        )
        IconButton(
            onClick = { onIncreaseValue() },
            modifier = Modifier
                .size(dimensionResource(R.dimen.icon_container_size))
                .clip(MaterialTheme.shapes.medium)
                .border(dimensionResource(R.dimen.border_width), Color.Gray, MaterialTheme.shapes.medium)
        ) {
            Icon(
                painter = painterResource(R.drawable.add_note_icon),
                contentDescription = stringResource(R.string.increase_icon_description),
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(dimensionResource(R.dimen.icon_size))
            )
        }
    }
}