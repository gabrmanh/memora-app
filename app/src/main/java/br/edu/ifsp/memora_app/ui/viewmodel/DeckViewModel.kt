package br.edu.ifsp.memora_app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.ifsp.memora_app.data.local.dto.DeckStatus
import br.edu.ifsp.memora_app.data.local.dto.DeckWithStatus
import br.edu.ifsp.memora_app.data.local.repository.DeckRepository
import br.edu.ifsp.memora_app.ui.config.SessionManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DeckViewModel(
    private val repository: DeckRepository
) : ViewModel() {

    private val userId = SessionManager.requireUserId()


    val decksWithStatus: StateFlow<List<DeckWithStatus>> =
        repository.getDecksWithStatus(userId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    // Grouped by status for UI
    val groupedDecks: StateFlow<Map<DeckStatus, List<DeckWithStatus>>> =
        decksWithStatus.map { decks ->
            decks.groupBy { it.status }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyMap()
        )

    fun refreshDecks() {
        // Room Flow auto-updates, but you can force refresh if needed
        viewModelScope.launch {
            // Trigger any manual refresh logic
        }
    }
}
