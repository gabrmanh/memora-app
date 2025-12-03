package br.edu.ifsp.memora_app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.ifsp.memora_app.data.local.dao.FieldDao
import br.edu.ifsp.memora_app.domain.deck.Field
import br.edu.ifsp.memora_app.domain.deck.FieldRole
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class FieldsEditorViewModel(
    private val fieldDao: FieldDao,
    private val deckId: String
) : ViewModel() {

    val fields: StateFlow<List<Field>> = fieldDao.getFieldsForDeck(deckId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addField(name: String, role: FieldRole) {
        viewModelScope.launch {
            val field = Field(
                id = UUID.randomUUID().toString(),
                name = name,
                deckId = deckId,
                role = role
            )
            fieldDao.insert(field)
        }
    }

    fun updateField(field: Field) {
        viewModelScope.launch {
            fieldDao.update(field)
        }
    }

    fun deleteField(field: Field) {
        viewModelScope.launch {
            fieldDao.delete(field)
        }
    }
}

