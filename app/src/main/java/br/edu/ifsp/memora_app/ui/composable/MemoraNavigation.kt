package br.edu.ifsp.memora_app.ui.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import br.edu.ifsp.memora_app.ui.composable.deck.DeckEditorScreen
import br.edu.ifsp.memora_app.ui.composable.deck.card.CardEditScreen
import br.edu.ifsp.memora_app.ui.composable.deck.card.CardsEditorScreen
import br.edu.ifsp.memora_app.ui.composable.deck.field.FieldsEditorScreen
import br.edu.ifsp.memora_app.ui.composable.home.MemoraHomeScreen
import br.edu.ifsp.memora_app.ui.composable.study_session.StudySessionScreen
import br.edu.ifsp.memora_app.ui.composable.study_session.StudySessionStartScreen

@Composable
fun MemoraNavigation() {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }
    var newDeckCounter by remember { mutableIntStateOf(0) }  // Track new deck creations

    when (val screen = currentScreen) {
        Screen.Home -> {
            MemoraHomeScreen(
                onNavigateToDeckEditor = { deckId ->
                    if (deckId == null) {
                        newDeckCounter++  // Increment for each new deck
                    }
                    currentScreen = Screen.DeckEditor(deckId, newDeckCounter)
                },
                onNavigateToStudyStart = { deckId ->
                    currentScreen = Screen.StudySessionStart(deckId)
                }
            )
        }
        is Screen.DeckEditor -> {
            DeckEditorScreen(
                deckId = screen.deckId,
                creationCounter = screen.creationCounter,  // Pass counter
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
                    currentScreen = Screen.DeckEditor(screen.deckId, 0)
                }
            )
        }
        is Screen.CardsEditor -> {
            CardsEditorScreen(
                deckId = screen.deckId,
                onNavigateBack = {
                    currentScreen = Screen.DeckEditor(screen.deckId, 0)
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
        is Screen.StudySessionStart -> {
            StudySessionStartScreen(
                deckId = screen.deckId,
                onNavigateBack = { currentScreen = Screen.Home },
                onStartStudy = {
                    currentScreen = Screen.StudySession(screen.deckId)
                }
            )
        }
        is Screen.StudySession -> {
            StudySessionScreen(
                deckId = screen.deckId,
                onNavigateBack = { currentScreen = Screen.Home }
            )
        }
    }
}

sealed class Screen {
    data object Home : Screen()
    data class DeckEditor(val deckId: String?, val creationCounter: Int = 0) : Screen()  // Add counter
    data class FieldsEditor(val deckId: String) : Screen()
    data class CardsEditor(val deckId: String) : Screen()
    data class CardEdit(val cardId: String, val deckId: String) : Screen()
    data class StudySessionStart(val deckId: String) : Screen()
    data class StudySession(val deckId: String) : Screen()
}