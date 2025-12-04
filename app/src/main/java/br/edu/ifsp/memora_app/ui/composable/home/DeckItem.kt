package br.edu.ifsp.memora_app.ui.composable.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.edu.ifsp.memora_app.data.local.dto.DeckStatus
import br.edu.ifsp.memora_app.data.local.dto.DeckWithStatus

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DeckItem(
    deckWithStatus: DeckWithStatus,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = deckWithStatus.deck.name.take(2).uppercase(),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = deckWithStatus.deck.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = getStatusText(deckWithStatus),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (deckWithStatus.pendingCount > 0) {
                Badge(
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Text(
                        text = deckWithStatus.pendingCount.toString(),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}

fun getStatusText(deckWithStatus: DeckWithStatus): String {
    return when (deckWithStatus.status) {
        DeckStatus.PENDING -> "${deckWithStatus.pendingCount} due now"
        DeckStatus.WAITING -> "${deckWithStatus.totalCards} cards learning"
        DeckStatus.NEW -> "${deckWithStatus.totalCards} new cards"
        DeckStatus.FINISHED -> "All ${deckWithStatus.totalCards} mastered!"
    }
}

