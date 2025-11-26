package br.edu.ifsp.memora_app.domain

import br.edu.ifsp.memora_app.domain.deck.CardProgress
import br.edu.ifsp.memora_app.domain.deck.CardState

object SpacedRepetitionAlgorithm {

    private const val GRADUATION_INTERVAL = 1    // days
    private const val MASTERY_THRESHOLD = 60     // days

    fun processAnswer(
        progress: CardProgress,
        result: AnswerResult
    ): CardProgress {
        return when (progress.state) {
            CardState.NEW      -> processNewCard(progress, result)
            CardState.LEARNING -> processLearningCard(progress, result)
            CardState.REVIEWING,
            CardState.MASTERED -> processReviewingCard(progress, result)
        }
    }

    private fun processNewCard(
        progress: CardProgress,
        result: AnswerResult
    ): CardProgress {
        return when (result) {
            AnswerResult.FIRST_CORRECT -> {
                progress.copy(
                    state = CardState.LEARNING,
                    attemptsInSession = 1,
                    nextReviewDate = now() + 10.minutes(),
                    lastReviewDate = now(),
                    totalCorrect = progress.totalCorrect + 1
                )
            }
            AnswerResult.SECOND_CORRECT -> {
                progress.copy(
                    state = CardState.LEARNING,
                    attemptsInSession = 2,
                    nextReviewDate = now() + 10.minutes(),
                    lastReviewDate = now(),
                    totalCorrect = progress.totalCorrect + 1,
                    totalIncorrect = progress.totalIncorrect + 1
                )
            }
            AnswerResult.WRONG_BOTH -> {
                progress.copy(
                    state = CardState.NEW,
                    attemptsInSession = 0,
                    nextReviewDate = now() + 1.days(),
                    lastReviewDate = now(),
                    totalIncorrect = progress.totalIncorrect + 2
                )
            }
        }
    }

    private fun processLearningCard(
        progress: CardProgress,
        result: AnswerResult
    ): CardProgress {
        return when (result) {
            AnswerResult.FIRST_CORRECT -> {
                progress.copy(
                    state = CardState.REVIEWING,
                    interval = GRADUATION_INTERVAL,
                    repetitions = progress.repetitions + 1,
                    attemptsInSession = 0,
                    nextReviewDate = now() + GRADUATION_INTERVAL.days(),
                    lastReviewDate = now(),
                    totalCorrect = progress.totalCorrect + 1
                )
            }
            AnswerResult.SECOND_CORRECT -> {
                progress.copy(
                    state = CardState.REVIEWING,
                    interval = 1, // keep at 1 day
                    repetitions = progress.repetitions + 1,
                    attemptsInSession = 0,
                    nextReviewDate = now() + 1.days(),
                    lastReviewDate = now(),
                    totalCorrect = progress.totalCorrect + 1,
                    totalIncorrect = progress.totalIncorrect + 1
                )
            }
            AnswerResult.WRONG_BOTH -> {
                // Failed: stay in learning, try again tomorrow
                progress.copy(
                    state = CardState.LEARNING,
                    attemptsInSession = 0,
                    nextReviewDate = now() + 1.days(),
                    lastReviewDate = now(),
                    totalIncorrect = progress.totalIncorrect + 2
                )
            }
        }
    }

    private fun processReviewingCard(
        progress: CardProgress,
        result: AnswerResult
    ): CardProgress {
        return when (result) {
            AnswerResult.FIRST_CORRECT -> {
                // Increase interval faster
                val newInterval = (progress.interval * 2).coerceAtLeast(1)
                val newState =
                    if (newInterval >= MASTERY_THRESHOLD) CardState.MASTERED else CardState.REVIEWING

                progress.copy(
                    state = newState,
                    interval = newInterval,
                    repetitions = progress.repetitions + 1,
                    attemptsInSession = 0,
                    nextReviewDate = now() + newInterval.days(),
                    lastReviewDate = now(),
                    totalCorrect = progress.totalCorrect + 1
                )
            }
            AnswerResult.SECOND_CORRECT -> {
                // Slower increase
                val newInterval = (progress.interval * 1.5).toInt().coerceAtLeast(1)
                val newState =
                    if (newInterval >= MASTERY_THRESHOLD) CardState.MASTERED else CardState.REVIEWING

                progress.copy(
                    state = newState,
                    interval = newInterval,
                    repetitions = progress.repetitions + 1,
                    attemptsInSession = 0,
                    nextReviewDate = now() + newInterval.days(),
                    lastReviewDate = now(),
                    totalCorrect = progress.totalCorrect + 1,
                    totalIncorrect = progress.totalIncorrect + 1
                )
            }
            AnswerResult.WRONG_BOTH -> {
                // Drop back to learning and see soon
                progress.copy(
                    state = CardState.LEARNING,
                    interval = 0,
                    repetitions = 0,
                    attemptsInSession = 0,
                    nextReviewDate = now() + 10.minutes(),
                    lastReviewDate = now(),
                    totalIncorrect = progress.totalIncorrect + 2
                )
            }
        }
    }

    private fun now(): Long = System.currentTimeMillis()
}

enum class AnswerResult {
    FIRST_CORRECT,   // Correct on first try for this review
    SECOND_CORRECT,  // Wrong first, correct second
    WRONG_BOTH       // Wrong on both attempts (or gave up)
}

// Reuse your helpers
private fun Int.days(): Long = this * 24 * 60 * 60 * 1000L
private fun Int.minutes(): Long = this * 60 * 1000L
