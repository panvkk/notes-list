package com.example.noteslist.presentation.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.noteslist.R

@Composable
fun SettingValueCard(
    value: Int,
    onIncreaseValue: () -> Unit,
    onDecreaseValue: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Card(
            shape = MaterialTheme.shapes.medium,
            border = CardDefaults.outlinedCardBorder(),
            modifier = Modifier.clickable { onDecreaseValue() }
                .size(52.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Icon(
                    painter = painterResource(R.drawable.decrease_icon),
                    contentDescription = stringResource(R.string.decrease_icon_description),
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(40.dp).align(Alignment.Center)
                )
            }
        }
        Text(
            text = "$value",
            style = MaterialTheme.typography.bodyMedium
        )
        Card(
            shape = MaterialTheme.shapes.medium,
            border = CardDefaults.outlinedCardBorder(),
            modifier = Modifier.clickable { onIncreaseValue() }
                .size(52.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Icon(
                    painter = painterResource(R.drawable.add_note_icon),
                    contentDescription = stringResource(R.string.increase_icon_description),
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(40.dp).align(Alignment.Center)
                )
            }
        }
    }
}