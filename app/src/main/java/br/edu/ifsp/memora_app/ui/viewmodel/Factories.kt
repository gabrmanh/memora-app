package br.edu.ifsp.memora_app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.edu.ifsp.memora_app.data.local.dao.CardDao
import br.edu.ifsp.memora_app.data.local.dao.DeckDao
import br.edu.ifsp.memora_app.data.local.dao.FieldDao
import br.edu.ifsp.memora_app.data.local.dao.FieldValueDao
import br.edu.ifsp.memora_app.data.local.dao.UserDao
import br.edu.ifsp.memora_app.data.local.repository.DeckRepository

class DeckViewModelFactory(
    private val repository: DeckRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DeckViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DeckViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class AuthViewModelFactory(
    private val userDao: UserDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(userDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class DeckEditorViewModelFactory(
    private val deckDao: DeckDao,
    private val deckId: String?
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DeckEditorViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DeckEditorViewModel(deckDao, deckId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class FieldsEditorViewModelFactory(
    private val fieldDao: FieldDao,
    private val deckId: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FieldsEditorViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FieldsEditorViewModel(fieldDao, deckId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class CardsEditorViewModelFactory(
    private val cardDao: CardDao,
    private val fieldDao: FieldDao,
    private val fieldValueDao: FieldValueDao,
    private val deckId: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CardsEditorViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CardsEditorViewModel(cardDao, fieldDao, fieldValueDao, deckId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class CardEditViewModelFactory(
    private val cardDao: CardDao,
    private val fieldDao: FieldDao,
    private val fieldValueDao: FieldValueDao,
    private val cardId: String,
    private val deckId: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CardEditViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CardEditViewModel(cardDao, fieldDao, fieldValueDao, cardId, deckId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}