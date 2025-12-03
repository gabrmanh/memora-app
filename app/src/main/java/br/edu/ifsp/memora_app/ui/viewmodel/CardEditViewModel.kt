package br.edu.ifsp.memora_app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.ifsp.memora_app.data.local.dao.CardDao
import br.edu.ifsp.memora_app.data.local.dao.FieldDao
import br.edu.ifsp.memora_app.data.local.dao.FieldValueDao
import br.edu.ifsp.memora_app.domain.deck.Field
import br.edu.ifsp.memora_app.domain.deck.FieldValue
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class CardEditViewModel(
    private val cardDao: CardDao,
    private val fieldDao: FieldDao,
    private val fieldValueDao: FieldValueDao,
    private val cardId: String,
    private val deckId: String
) : ViewModel() {

    val fields: StateFlow<List<Field>> = fieldDao.getFieldsForDeck(deckId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val fieldValues: StateFlow<List<FieldValue>> = fieldValueDao.getFieldValuesForCard(cardId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val pendingUpdates = mutableMapOf<String, String>()

    fun updateFieldValue(fieldId: String, value: String) {
        pendingUpdates[fieldId] = value
    }

    fun saveCard() {
        viewModelScope.launch {
            pendingUpdates.forEach { (fieldId, value) ->
                val existingValue = fieldValues.value.find { it.fieldId == fieldId }

                if (existingValue != null) {
                    fieldValueDao.update(existingValue.copy(value = value))
                } else {
                    val newFieldValue = FieldValue(
                        value = value,
                        cardId = cardId,
                        fieldId = fieldId
                    )
                    fieldValueDao.insert(newFieldValue)
                }
            }
            pendingUpdates.clear()
        }
    }
}
