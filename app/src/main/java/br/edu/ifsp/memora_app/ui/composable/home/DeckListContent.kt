package br.edu.ifsp.memora_app.ui.composable.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import br.edu.ifsp.memora_app.data.local.dto.DeckStatus
import br.edu.ifsp.memora_app.data.local.dto.DeckWithStatus
import br.edu.ifsp.memora_app.domain.deck.Deck

@Composable
fun DeckListContent(
    decks: List<DeckWithStatus>,
    onDeckClick: (Deck) -> Unit,
    onDeckLongClick: (Deck) -> Unit
) {
    val groupedDecks = decks.groupBy { it.status }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        DeckStatus.entries.forEach { status ->
            val decksForStatus = groupedDecks[status] ?: emptyList()

            if (decksForStatus.isNotEmpty()) {
                item {
                    StatusSectionHeader(status = status)
                }

                items(decksForStatus) { deckWithStatus ->
                    DeckItem(
                        deckWithStatus = deckWithStatus,
                        onClick = { onDeckClick(deckWithStatus.deck) },
                        onLongClick = { onDeckLongClick(deckWithStatus.deck) }
                    )
                }
            }
        }
    }
}