package br.edu.ifsp.memora_app.ui.composable.study_session


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.edu.ifsp.memora_app.data.local.AppDatabase
import br.edu.ifsp.memora_app.ui.composable.deck.DeckEditorTopBar
import br.edu.ifsp.memora_app.ui.viewmodel.StudySessionStartViewModel
import br.edu.ifsp.memora_app.ui.viewmodel.StudySessionStartViewModelFactory

@Composable
fun StudySessionStartScreen(
    deckId: String,
    onNavigateBack: () -> Unit,
    onStartStudy: () -> Unit
) {
    val context = LocalContext.current
    val database = remember { AppDatabase.getInstance(context) }
    val viewModel: StudySessionStartViewModel = viewModel(
        key = "study_start_$deckId",
        factory = StudySessionStartViewModelFactory(
            database.deckDao(),
            database.cardProgressDao(),
            deckId
        )
    )

    val sessionInfo by viewModel.sessionInfo.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        DeckEditorTopBar(
            title = sessionInfo?.deckName ?: "Loading...",
            onNavigateBack = onNavigateBack
        )

        sessionInfo?.let { info ->
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 24.dp,
                    end = 24.dp,
                    top = 24.dp,
                    bottom = 80.dp  // Extra space at bottom
                ),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item { Spacer(modifier = Modifier.height(16.dp)) }

                // Cards ready to study
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "${info.cardsReady}",
                                style = MaterialTheme.typography.displayLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = "cards ready to study",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }

                // Next unlock time
                if (info.nextCardUnlockTime != null && info.cardsReady == 0) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Next card in",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                                Text(
                                    text = formatTimeUntil(info.nextCardUnlockTime),
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                        }
                    }
                }

                // Statistics grid
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCard(
                            modifier = Modifier.weight(1f),
                            label = "New",
                            value = info.newCards,
                            color = MaterialTheme.colorScheme.tertiaryContainer
                        )
                        StatCard(
                            modifier = Modifier.weight(1f),
                            label = "Learning",
                            value = info.learningCards,
                            color = MaterialTheme.colorScheme.tertiaryContainer
                        )
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCard(
                            modifier = Modifier.weight(1f),
                            label = "Reviewing",
                            value = info.reviewingCards,
                            color = MaterialTheme.colorScheme.tertiaryContainer
                        )
                        StatCard(
                            modifier = Modifier.weight(1f),
                            label = "Mastered",
                            value = info.masteredCards,
                            color = MaterialTheme.colorScheme.tertiaryContainer
                        )
                    }
                }

                // Total cards
                item {
                    Text(
                        text = "Total: ${info.totalCards} cards",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }

                // Spacer to push button to bottom
                item { Spacer(modifier = Modifier.height(24.dp)) }

                // Start button
                item {
                    Button(
                        onClick = onStartStudy,
                        enabled = info.cardsReady > 0,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (info.cardsReady > 0) "Start Studying" else "No Cards Ready",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        } ?: run {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}
