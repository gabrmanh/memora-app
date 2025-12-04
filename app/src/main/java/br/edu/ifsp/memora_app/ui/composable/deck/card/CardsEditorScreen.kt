package br.edu.ifsp.memora_app.ui.composable.deck.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.edu.ifsp.memora_app.data.local.AppDatabase
import br.edu.ifsp.memora_app.ui.composable.deck.DeckEditorTopBar
import br.edu.ifsp.memora_app.ui.viewmodel.CardsEditorViewModel
import br.edu.ifsp.memora_app.ui.viewmodel.CardsEditorViewModelFactory


@Composable
fun CardsEditorScreen(
    deckId: String,
    onNavigateBack: () -> Unit,
    onEditCard: (String) -> Unit
) {
    val context = LocalContext.current
    val database = remember { AppDatabase.getInstance(context) }
    val viewModel: CardsEditorViewModel = viewModel(
        key = "cards_$deckId",
        factory = CardsEditorViewModelFactory(
            database.cardDao(),
            database.fieldDao(),
            database.fieldValueDao(),
            database.cardProgressDao(),
            deckId
        )
    )

    val cards by viewModel.cards.collectAsState()
    val fields by viewModel.fields.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        DeckEditorTopBar(
            title = "Cards (${cards.size})",
            onNavigateBack = onNavigateBack
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            items(cards) { card ->
                CardItem(
                    card = card,
                    fields = fields,
                    viewModel = viewModel,
                    onEdit = { onEditCard(card.id) },
                    onDelete = { viewModel.deleteCard(card) }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (cards.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No cards yet. Add one to get started!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        Button(
            onClick = { viewModel.addCard() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .padding(bottom = 32.dp)  // Extra bottom padding
                .height(50.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Card"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Add Card")
        }
    }
}