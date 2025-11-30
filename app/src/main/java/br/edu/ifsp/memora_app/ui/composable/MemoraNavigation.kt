package br.edu.ifsp.memora_app.ui.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import br.edu.ifsp.memora_app.ui.composable.deck.DeckEditorScreen
import br.edu.ifsp.memora_app.ui.composable.deck.field.FieldsEditorScreen
import br.edu.ifsp.memora_app.ui.composable.home.MemoraHomeScreen

@Composable
fun MemoraNavigation() {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }

    when (val screen = currentScreen) {
        Screen.Home -> {
            MemoraHomeScreen(
                onNavigateToDeckEditor = { deckId ->
                    currentScreen = Screen.DeckEditor(deckId)
                }
            )
        }
        is Screen.DeckEditor -> {
            DeckEditorScreen(
                deckId = screen.deckId,
                onNavigateBack = {
                    currentScreen = Screen.Home
                },
                onNavigateToCards = { deckId ->
                    // TODO: Navigate to cards screen
                },
                onNavigateToFields = { deckId ->
                    currentScreen = Screen.FieldsEditor(deckId)
                }
            )
        }
        is Screen.FieldsEditor -> {
            FieldsEditorScreen(
                deckId = screen.deckId,
                onNavigateBack = {
                    currentScreen = Screen.DeckEditor(screen.deckId)
                }
            )
        }

    }
}

sealed class Screen {
    object Home : Screen()
    data class DeckEditor(val deckId: String?) : Screen()
    data class FieldsEditor(val deckId: String) : Screen()
    // Add more screens as needed
    // data class Cards(val deckId: String) : Screen()
    // data class Fields(val deckId: String) : Screen()
}
