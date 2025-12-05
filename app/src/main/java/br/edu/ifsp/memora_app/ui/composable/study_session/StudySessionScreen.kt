package br.edu.ifsp.memora_app.ui.composable.study_session

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import br.edu.ifsp.memora_app.data.local.AppDatabase
import br.edu.ifsp.memora_app.data.local.dto.StudyState
import br.edu.ifsp.memora_app.ui.viewmodel.StudySessionViewModel
import br.edu.ifsp.memora_app.ui.viewmodel.StudySessionViewModelFactory


@Composable
fun StudySessionScreen(
    deckId: String,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val database = remember { AppDatabase.getInstance(context) }
    val viewModel: StudySessionViewModel = viewModel(
        key = "study_$deckId",
        factory = StudySessionViewModelFactory(
            database.cardDao(),
            database.cardProgressDao(),
            database.fieldDao(),
            database.fieldValueDao(),
            deckId
        )
    )

    val studyState by viewModel.studyState.collectAsState()
    val cardsRemaining by viewModel.cardsRemaining.collectAsState()
    val totalCards by viewModel.totalCards.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        // Top bar with progress
        StudyTopBar(
            onNavigateBack = {
                viewModel.endSession()
                onNavigateBack()
            },
            progress = if (totalCards > 0) {
                (totalCards - cardsRemaining).toFloat() / totalCards
            } else 0f,
            cardsCompleted = totalCards - cardsRemaining,
            totalCards = totalCards
        )

        when (val state = studyState) {
            is StudyState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is StudyState.ShowingQuestion -> {
                QuestionCard(
                    studyCard = state.card,
                    attempt = state.currentAttempt,
                    onSubmitAnswer = { answer ->
                        viewModel.submitAnswer(answer)
                    }
                )
            }

            is StudyState.ShowingAnswer -> {
                AnswerCard(
                    studyCard = state.card,
                    userAnswer = state.userAnswer,
                    isCorrect = state.isCorrect,
                    attempt = state.currentAttempt,
                    onContinue = {
                        viewModel.nextCard()
                    }
                )
            }

            is StudyState.Completed -> {
                CompletedScreen(
                    studiedCount = state.studiedCount,
                    onFinish = onNavigateBack
                )
            }
        }
    }
}
