package br.edu.ifsp.memora_app.ui.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import br.edu.ifsp.memora_app.ui.composable.deck.DeckEditorScreen
import br.edu.ifsp.memora_app.ui.composable.deck.card.CardEditScreen
import br.edu.ifsp.memora_app.ui.composable.deck.card.CardsEditorScreen
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
                    currentScreen = Screen.CardsEditor(deckId)
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
        is Screen.CardsEditor -> {
            CardsEditorScreen(
                deckId = screen.deckId,
                onNavigateBack = {
                    currentScreen = Screen.DeckEditor(screen.deckId)
                },
                onEditCard = { cardId ->
                    currentScreen = Screen.CardEdit(cardId, screen.deckId)
                }
            )
        }
        is Screen.CardEdit -> {
            CardEditScreen(
                cardId = screen.cardId,
                deckId = screen.deckId,
                onNavigateBack = {
                    currentScreen = Screen.CardsEditor(screen.deckId)
                }
            )
        }
    }
}

sealed class Screen {
    data object Home : Screen()
    data class DeckEditor(val deckId: String?) : Screen()
    data class FieldsEditor(val deckId: String) : Screen()
    data class CardsEditor(val deckId: String) : Screen()
    data class CardEdit(val cardId: String, val deckId: String) : Screen()
}
