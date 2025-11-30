package br.edu.ifsp.memora_app.ui.composable.deck

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.edu.ifsp.memora_app.data.local.AppDatabase
import br.edu.ifsp.memora_app.ui.viewmodel.DeckEditorViewModel
import br.edu.ifsp.memora_app.ui.viewmodel.DeckEditorViewModelFactory


@Composable
fun DeckEditorScreen(
    deckId: String? = null,
    onNavigateBack: () -> Unit,
    onNavigateToCards: (String) -> Unit,
    onNavigateToFields: (String) -> Unit
) {
    val context = LocalContext.current
    val database = remember { AppDatabase.getInstance(context) }
    val viewModel: DeckEditorViewModel = viewModel(
        key = "deck_${deckId ?: "new"}",
        factory = DeckEditorViewModelFactory(database.deckDao(), deckId)
    )
    val deckName by viewModel.deckName.collectAsState()
    val deckDescription by viewModel.deckDescription.collectAsState()
    val currentDeckId by viewModel.currentDeckId.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        DeckEditorTopBar(
            title = if (deckId == null) "New Deck" else "Edit Deck",
            onNavigateBack = onNavigateBack
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            SettingItem(
                label = "NAME",
                value = deckName,
                isEditable = true,
                onValueChange = { viewModel.updateName(it) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            SettingItem(
                label = "DESCRIPTION",
                value = deckDescription ?: "Add description",
                isEditable = true,
                onValueChange = { viewModel.updateDescription(it) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            SettingRow(
                label = "FIELDS",
                value = "Edit fields",
                onClick = {
                    currentDeckId?.let { onNavigateToFields(it) }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            SettingRow(
                label = "CARDS",
                value = "Manage cards",
                onClick = {
                    currentDeckId?.let { onNavigateToCards(it) }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.exportDeck(context) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Export",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Export Deck")
            }
        }
    }
}

