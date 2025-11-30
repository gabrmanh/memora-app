package br.edu.ifsp.memora_app.ui.composable.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import br.edu.ifsp.memora_app.data.local.AppDatabase
import br.edu.ifsp.memora_app.data.local.repository.DeckRepository
import br.edu.ifsp.memora_app.ui.viewmodel.DeckViewModel
import br.edu.ifsp.memora_app.ui.viewmodel.DeckViewModelFactory

@Composable
fun MemoraHomeScreen(
    onNavigateToDeckEditor: (String?) -> Unit
) {
    val context = LocalContext.current
    val database = remember { AppDatabase.getInstance(context) }
    val repository = remember {
        DeckRepository(database.deckDao(), database.cardProgressDao())
    }
    val viewModel: DeckViewModel = viewModel(
        factory = DeckViewModelFactory(repository)
    )

    val decksWithStatus by viewModel.decksWithStatus.collectAsState()

    Scaffold(
        topBar = {
            MemoraTopBar(onCreateDeck = { onNavigateToDeckEditor(null) })
        },
        bottomBar = {
            MemoraBottomNav(selectedItem = "home", onItemSelected = { })
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            DeckListContent(
                decks = decksWithStatus,
                onDeckClick = { deck -> onNavigateToDeckEditor(deck.id) }
            )
        }
    }
}