package com.example.project_vastuapp.datalayer

import android.util.Log
import java.lang.Exception

class VastuLogic(private val vastuRepo: VastuRepository) {

    // ✅ Callback interface uses FurnitureAnalysisOutput
    interface VastuOutputCallback {
        fun onAnalysisComplete(outputList: List<FurnitureAnalysisOutput>)
        fun onError(exception: Exception)
    }

    /**
     * Processes a list of FurnitureInput and evaluates each item against Vastu rules.
     * Calls the callback when complete.
     */
    fun evaluateFurnitureInputList(
        furnitureInputList: List<FurnitureInput>,
        callback: VastuOutputCallback
    ) {
        val evaluatedFurnitureObjects = mutableListOf<FurnitureAnalysisOutput>()
        val totalItems = furnitureInputList.size
        var itemsProcessed = 0

        // Recursive function to process items sequentially
        fun processNextItem() {
            if (itemsProcessed >= totalItems) {
                callback.onAnalysisComplete(evaluatedFurnitureObjects)
                return
            }

            val input = furnitureInputList[itemsProcessed]
            checkFurnitureDirection(input) { output ->
                evaluatedFurnitureObjects.add(output)
                itemsProcessed++
                processNextItem()
            }
        }

        processNextItem() // Start processing
    }

    /**
     * Fetches the rule from repository and compares with the input direction.
     */
    private fun checkFurnitureDirection(
        input: FurnitureInput,
        onComplete: (FurnitureAnalysisOutput) -> Unit // ✅ Corrected type here
    ) {
        vastuRepo.getFurnitureRule(input.furnitureType, object : VastuRepository.FurnitureRuleCallback {

            override fun onSuccess(rule: FurnitureRule?) {
                val output = if (rule == null || rule.type.isEmpty()) {
                    // No rule found for this furniture
                    FurnitureAnalysisOutput(
                        furnitureType = input.furnitureType,
                        reason = "No Vastu rule defined for this furniture type.",
                        isCorrect = false
                    )
                } else {
                    // Check if the user's direction is favorable
                    val isFavorable = rule.favorableDirections.any {
                        it.equals(input.direction, ignoreCase = true)
                    }

                    if (isFavorable) {
                        FurnitureAnalysisOutput(
                            furnitureType = input.furnitureType,
                            reason = rule.reasonfavorable,
                            isCorrect = true
                        )
                    } else {
                        FurnitureAnalysisOutput(
                            furnitureType = input.furnitureType,
                            reason = rule.reasonUnfavorable,
                            isCorrect = false
                        )
                    }
                }
                onComplete(output)
            }

            override fun onError(exception: Exception) {
                Log.e("VastuLogic", "Database error for ${input.furnitureType}: ${exception.message}")
                onComplete(
                    FurnitureAnalysisOutput(
                        furnitureType = input.furnitureType,
                        reason = "Error retrieving Vastu rules from the server.",
                        isCorrect = false
                    )
                )
            }
        })
    }
}
