package br.edu.ifsp.memora_app.ui.composable.deck.field

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.edu.ifsp.memora_app.domain.deck.Field
import br.edu.ifsp.memora_app.domain.deck.FieldRole


@Composable
fun FieldDialog(
    field: Field?,
    onDismiss: () -> Unit,
    onSave: (String, FieldRole) -> Unit
) {
    var name by remember { mutableStateOf(field?.name ?: "") }
    var selectedRole by remember { mutableStateOf(field?.role ?: FieldRole.FRONT) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(if (field == null) "Add Field" else "Edit Field")
        },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Field Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = { Text("e.g., Front, Back, Hint") }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Role selection
                Text(
                    text = "FIELD ROLE",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Front option
                    FilterChip(
                        selected = selectedRole == FieldRole.FRONT,
                        onClick = { selectedRole = FieldRole.FRONT },
                        label = { Text("Front") },
                        leadingIcon = if (selectedRole == FieldRole.FRONT) {
                            {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        } else null,
                        modifier = Modifier.weight(1f)
                    )

                    // Answer option
                    FilterChip(
                        selected = selectedRole == FieldRole.ANSWER,
                        onClick = { selectedRole = FieldRole.ANSWER },
                        label = { Text("Answer") },
                        leadingIcon = if (selectedRole == FieldRole.ANSWER) {
                            {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        } else null,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if (selectedRole == FieldRole.FRONT)
                        "Shown as prompt when studying"
                    else
                        "User must input this as answer",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontStyle = FontStyle.Italic
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onSave(name, selectedRole) },
                enabled = name.isNotBlank()
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
