package com.example.project_vastuapp.datalayer
import android.graphics.Rect
import android.graphics.Color

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FurnitureInput(
    val furnitureType: String,
    val direction: String
) : Parcelable
data class FurnitureAnalysisOutput (
    var furnitureType: String,
    var reason: String,
    var isCorrect: Boolean
)

data class FurnitureRule(
    // The type is used for validation and should match the document ID (e.g., "bed")
    val type: String = "",

    // Array of all correct placement directions (e.g., ["South", "West"])
    val favorableDirections: List<String> = emptyList(),

    // The reason to return if the placement IS correct
    val reasonfavorable: String = "",

    // The reason to return if the placement is NOT correct
    val reasonUnfavorable: String = ""
)


