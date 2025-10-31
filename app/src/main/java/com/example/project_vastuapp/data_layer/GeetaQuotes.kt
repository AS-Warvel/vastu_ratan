package com.example.project_vastuapp.data_layer

// GeetaService.kt
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await

object GeetaQuotesDataSource {

    private val db = FirebaseFirestore.getInstance()
    private val quotesCollection = db.collection("geeta_teachings")

    /**
     * TASK 1: Upload all Geeta quotes to Firestore.
     * This uses a WriteBatch to upload all quotes in a single atomic operation.
     */
    suspend fun uploadAllGeetaQuotes() {
        val querySnapshot = quotesCollection.get().await()
        if(querySnapshot.documents.isEmpty())
        {
            val quotesList = listOf(
                // User-provided quotes
                GeetaQuote(1, "It is better to live your own destiny imperfectly than to live an imitation of somebody else's life with perfection.", false),
                GeetaQuote(2, "A person can rise through the efforts of his own mind; or draw himself down, in the same manner. For each person is his own friend or his own enemy.", false),
                GeetaQuote(3, "You have the right to work, but never to the fruit of work.", false),
                GeetaQuote(4, "The soul is neither born, nor does it ever die.", false),
                GeetaQuote(5, "Man is made by his belief. As he believes, so he is.", false),
                GeetaQuote(6, "The wise grieve neither for the living nor for the dead.", false),
                GeetaQuote(7, "Perform your obligatory duty, because action is indeed better than inaction.", false),
                GeetaQuote(8, "Lust, anger, and greed are the three gates to self-destructive hell.", false),

                // Added quotes
                GeetaQuote(9, "Whatever happened, happened for the good. Whatever is happening, is happening for the good. Whatever will happen, will also happen for the good.", false),
                GeetaQuote(10, "Change is the law of the universe. You can be a millionaire, or a pauper in an instant.", false),
                GeetaQuote(11, "A man who sees action in inaction and inaction in action is intelligent among men.", false),
                GeetaQuote(12, "Set thy heart upon thy work, but never on its reward.", false),
                GeetaQuote(13, "There is neither this world, nor the world beyond, nor happiness for the one who doubts.", false)
            )

            try {
                val batch = db.batch()

                for (quote in quotesList) {
                    // Use the quote's ID as the document ID (as a string)
                    val docRef = quotesCollection.document(quote.id.toString())
                    batch.set(docRef, quote)
                }

                batch.commit().await()
                Log.d("GeetaService", "Successfully uploaded all Geeta quotes.")

            } catch (e: Exception) {
                Log.e("GeetaService", "Error uploading quotes", e)
            }
        }
        else {
            println(querySnapshot.documents)
        }

    }

    /**
     * TASK 2.1: Retrieve all Geeta quotes as a list.
     */
    suspend fun getGeetaQuotes(): List<GeetaQuote> {
        return try {
            val snapshot = quotesCollection.get().await()
            println(snapshot.documents.first())
            // Convert all documents to GeetaQuote objects, filtering out any potential nulls
            snapshot.documents.mapNotNull { doc ->
                val id = doc.getLong("id")?.toInt() ?: (doc.id.toInt() + 100)
                val quote = doc.getString("quote") ?: "nan"
                val isFavourite = doc.getBoolean("isFavourite") ?: false
                Log.d("ada", "asdasd: $doc")
                GeetaQuote(
                    id = id,
                    quote = quote,
                    isFavourite = isFavourite,
                )
//                it.toObject<GeetaQuote>()
            }
        } catch (e: Exception) {
            Log.e("GeetaService", "Error fetching quotes", e)
            emptyList() // Return an empty list on failure
        }
    }

    /**
     * TASK 2.2: Toggle the 'isFavourite' attribute for a specific quote.
     *
     * @param quoteId The ID of the quote document to update.
     * @param currentStatus The current 'isFavourite' status of the quote.
     */
    suspend fun toggleFavouriteStatus(quoteId: Int, currentStatus: Boolean) {

        try {
            val docRef = quotesCollection.document(quoteId.toString())
            // Update the 'isFavourite' field to the opposite of its current status
            docRef.update("isFavourite", !currentStatus).await()
            Log.d("GeetaService", "Quote $quoteId favourite status toggled., toggled to - ${!currentStatus}")
        } catch (e: Exception) {
            Log.e("GeetaService", "Error toggling favourite status for quote $quoteId", e)
        }
    }
}