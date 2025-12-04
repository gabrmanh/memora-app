package br.edu.ifsp.memora_app.ui.composable.deck.field

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.edu.ifsp.memora_app.data.local.AppDatabase
import br.edu.ifsp.memora_app.domain.deck.Field
import br.edu.ifsp.memora_app.ui.composable.deck.DeckEditorTopBar
import br.edu.ifsp.memora_app.ui.viewmodel.FieldsEditorViewModel
import br.edu.ifsp.memora_app.ui.viewmodel.FieldsEditorViewModelFactory

@Composable
fun FieldsEditorScreen(
    deckId: String,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val database = remember { AppDatabase.getInstance(context) }
    val viewModel: FieldsEditorViewModel = viewModel(
        key = "fields_$deckId",
        factory = FieldsEditorViewModelFactory(database.fieldDao(), deckId)
    )

    val fields by viewModel.fields.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var editingField by remember { mutableStateOf<Field?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        DeckEditorTopBar(
            title = "Edit Fields",
            onNavigateBack = onNavigateBack
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(fields) { field ->
                FieldItem(
                    field = field,
                    onEdit = { editingField = field },
                    onDelete = { viewModel.deleteField(field) }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (fields.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No fields yet. Add one to get started!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        Button(
            onClick = { showAddDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .padding(bottom = 32.dp)
                .height(50.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Field"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Add Field")
        }
    }

    if (showAddDialog || editingField != null) {
        FieldDialog(
            field = editingField,
            onDismiss = {
                showAddDialog = false
                editingField = null
            },
            onSave = { name, role ->
                if (editingField != null) {
                    viewModel.updateField(editingField!!.copy(name = name, role = role))
                } else {
                    viewModel.addField(name, role)
                }
                showAddDialog = false
                editingField = null
            }
        )
    }
}
