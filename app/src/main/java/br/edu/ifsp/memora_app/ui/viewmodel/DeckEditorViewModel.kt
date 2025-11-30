package br.edu.ifsp.memora_app.ui.viewmodel

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import br.edu.ifsp.memora_app.data.local.dao.DeckDao
import br.edu.ifsp.memora_app.domain.deck.Deck
import br.edu.ifsp.memora_app.ui.config.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class DeckEditorViewModel(
    private val deckDao: DeckDao,
    private val existingDeckId: String?
) : ViewModel() {

    private val _deckName = MutableStateFlow("New Deck")
    val deckName: StateFlow<String> = _deckName.asStateFlow()

    private val _deckDescription = MutableStateFlow<String?>(null)
    val deckDescription: StateFlow<String?> = _deckDescription.asStateFlow()

    // Initialize with existing ID or create new one immediately
    private val _currentDeckId = MutableStateFlow(existingDeckId ?: UUID.randomUUID().toString())
    val currentDeckId: StateFlow<String?> = _currentDeckId.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    init {
        existingDeckId?.let {
            loadDeck(it)
        } ?: run {
            // For new decks, save immediately with default values
            saveDeck()
        }
    }

    private fun loadDeck(deckId: String) {
        viewModelScope.launch {
            deckDao.getById(deckId)?.let { deck ->
                _deckName.value = deck.name
                _deckDescription.value = deck.description
            }
        }
    }

    fun updateName(name: String) {
        _deckName.value = name
        saveDeck()
    }

    fun updateDescription(description: String) {
        _deckDescription.value = description
        saveDeck()
    }

    fun saveDeck() {
        viewModelScope.launch {
            _isSaving.value = true

            val deck = Deck(
                id = _currentDeckId.value!!, // Now always has a value
                name = _deckName.value,
                description = _deckDescription.value,
                version = 1,
                createdBy = SessionManager.requireUserId()
            )

            deckDao.insert(deck)
            _isSaving.value = false
        }
    }

    fun exportDeck(context: Context) {
        viewModelScope.launch {
            val deckId = _currentDeckId.value ?: return@launch

            val exportData = """
                {
                    "name": "${_deckName.value}",
                    "description": "${_deckDescription.value ?: ""}",
                    "id": "$deckId"
                }
            """.trimIndent()

            shareText(context, exportData, _deckName.value)
        }
    }

    private fun shareText(context: Context, text: String, deckName: String) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            putExtra(Intent.EXTRA_TITLE, "$deckName - Memora Deck")
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, "Export Deck")
        context.startActivity(shareIntent)
    }
}
