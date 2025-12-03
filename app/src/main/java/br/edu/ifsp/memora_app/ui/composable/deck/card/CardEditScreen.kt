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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.edu.ifsp.memora_app.data.local.AppDatabase
import br.edu.ifsp.memora_app.ui.composable.deck.DeckEditorTopBar
import br.edu.ifsp.memora_app.ui.viewmodel.CardEditViewModel
import br.edu.ifsp.memora_app.ui.viewmodel.CardEditViewModelFactory


@Composable
fun CardEditScreen(
    cardId: String,
    deckId: String,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val database = remember { AppDatabase.getInstance(context) }
    val viewModel: CardEditViewModel = viewModel(
        key = "card_edit_$cardId",
        factory = CardEditViewModelFactory(
            database.cardDao(),
            database.fieldDao(),
            database.fieldValueDao(),
            cardId,
            deckId
        )
    )

    val fields by viewModel.fields.collectAsState()
    val fieldValues by viewModel.fieldValues.collectAsState()

    // Create local state for each field
    val localFieldValues = remember(fieldValues, fields) {
        mutableStateMapOf<String, String>().apply {
            fields.forEach { field ->
                val value = fieldValues.find { it.fieldId == field.id }?.value ?: ""
                put(field.id, value)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        DeckEditorTopBar(
            title = "Edit Card",
            onNavigateBack = {
                localFieldValues.forEach { (fieldId, value) ->
                    viewModel.updateFieldValue(fieldId, value)
                }
                viewModel.saveCard()
                onNavigateBack()
            }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            items(fields) { field ->
                val currentValue = localFieldValues[field.id] ?: ""

                Column {
                    Text(
                        text = field.name.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = currentValue,
                        onValueChange = { newValue ->
                            localFieldValues[field.id] = newValue
                        },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        maxLines = 5,
                        placeholder = { Text("Enter ${field.name.lowercase()}") }
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            if (fields.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "No fields defined for this deck",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Add fields in the deck settings first",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontStyle = FontStyle.Italic
                            )
                        }
                    }
                }
            }
        }
    }
}