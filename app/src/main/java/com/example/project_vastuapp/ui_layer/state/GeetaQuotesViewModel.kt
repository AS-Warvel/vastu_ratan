package com.example.project_vastuapp.ui_layer.state

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

// Data class to represent a single quote
data class GeetaQuote(
    val id: Int,
    val text: String,
    val isFavourite: Boolean = false
)


class GeetaViewModel : ViewModel() {

    // Private mutable state
    private val _quotes = mutableStateOf<List<GeetaQuote>>(emptyList())
    // Public immutable state for the UI to observe
    val quotes: State<List<GeetaQuote>> = _quotes

    init {
        // Load the initial list of quotes when the ViewModel is created
        _quotes.value = getInitialQuotes()
    }

    // Function to handle the toggle favourite action
    fun toggleFavourite(quoteId: Int) {
        val currentQuotes = _quotes.value
        _quotes.value = currentQuotes.map { quote ->
            if (quote.id == quoteId) {
                quote.copy(isFavourite = !quote.isFavourite)
            } else {
                quote
            }
        }
    }

    // Sample data for the quotes
    private fun getInitialQuotes(): List<GeetaQuote> {
        return listOf(
            GeetaQuote(1, "It is better to live your own destiny imperfectly than to live an imitation of somebody else's life with perfection."),
            GeetaQuote(2, "A person can rise through the efforts of his own mind; or draw himself down, in the same manner. For each person is his own friend or his own enemy.", isFavourite = true),
            GeetaQuote(3, "You have the right to work, but never to the fruit of work."),
            GeetaQuote(4, "The soul is neither born, nor does it ever die."),
            GeetaQuote(5, "Man is made by his belief. As he believes, so he is.", isFavourite = true),
            GeetaQuote(6, "The wise grieve neither for the living nor for the dead."),
            GeetaQuote(7, "Perform your obligatory duty, because action is indeed better than inaction."),
            GeetaQuote(8, "Lust, anger, and greed are the three gates to self-destructive hell.")
        )
    }
}