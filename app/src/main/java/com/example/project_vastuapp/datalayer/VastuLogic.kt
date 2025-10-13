

package com.example.project_vastuapp.datalayer

// Imports for supporting classes from the same package
import com.example.project_vastuapp.datalayer.VastuRepository
import com.example.project_vastuapp.datalayer.FurnitureInput
import com.example.project_vastuapp.datalayer.FurnitureOutput

import android.util.Log
class VastuLogic(private val vastuRepo: VastuRepository) {

    // Define a callback for the final output, as the process is asynchronous
    interface VastuOutputCallback {
        fun onAnalysisComplete(outputList: List<FurnitureOutput>)
        fun onError(exception: Exception)
    }

    /**
     * Processes a list of user inputs by checking each against the rules in Firebase.
     * Uses recursion to handle the list of asynchronous calls sequentially.
     */
    fun evaluateFurnitureInputList(
        furnitureInputList: List<FurnitureInput>,
        callback: VastuOutputCallback
    ) {
        val evaluatedFurnitureObjects = mutableListOf<FurnitureOutput>()
        val totalItems = furnitureInputList.size
        var itemsProcessed = 0

        // Start of the recursive process function
        fun processNextItem() {
            if (itemsProcessed >= totalItems) {
                // Base case: All items processed, return the final list
                callback.onAnalysisComplete(evaluatedFurnitureObjects)
                return
            }

            val input = furnitureInputList[itemsProcessed]
            checkFurnitureDirection(input) { output ->
                evaluatedFurnitureObjects.add(output)
                itemsProcessed++
                processNextItem() // Process the next item in the list
            }
        }

        processNextItem() // Start the process by calling the first item
    }

    /**
     * Core Vastu Rule: Fetches the rule from the repository and compares the direction.
     */
    private fun checkFurnitureDirection(input: FurnitureInput, onComplete: (FurnitureOutput) -> Unit) {

        // Use the repository to fetch the data
        vastuRepo.getFurnitureRule(input.furnitureType, object : VastuRepository.FurnitureRuleCallback {

            override fun onSuccess(rule: FurnitureRule?) {
                val output = if (rule == null || rule.type.isEmpty()) {
                    // Case 1: No rule found for this furniture type (the 'else' case in your original logic)
                    FurnitureOutput(
                        furnitureType = input.furnitureType,
                        reason = "No Vastu rule defined for this furniture type.",
                        isCorrect = false
                    )
                } else {
                    // **BUSINESS RULE APPLICATION:**
                    // Check if the user's direction is one of the favorable directions in the rule.
                    val isFavorable = rule.favorableDirections.any {
                        // Comparison should be case-insensitive
                        it.equals(input.direction, ignoreCase = true)
                    }

                    if (isFavorable) {
                        // Case 2: Direction is correct
                        FurnitureOutput(
                            furnitureType = input.furnitureType,
                            reason = rule.reasonfavorable, // Use the correct reason from Firebase
                            isCorrect = true
                        )
                    } else {
                        // Case 3: Direction is incorrect (unfavorable)
                        FurnitureOutput(
                            furnitureType = input.furnitureType,
                            reason = rule.reasonUnfavorable, // Use the incorrect reason from Firebase
                            isCorrect = false
                        )
                    }
                }
                onComplete(output) // Pass the result back to the recursive function
            }

            override fun onError(exception: Exception) {
                // Handle database errors (network issues, permissions, etc.)
                Log.e("VastuLogic", "Database error for ${input.furnitureType}: ${exception.message}")
                onComplete(
                    FurnitureOutput(
                        furnitureType = input.furnitureType,
                        reason = "Error retrieving Vastu rules from the server.",
                        isCorrect = false
                    )
                )
            }
        })
    }
}