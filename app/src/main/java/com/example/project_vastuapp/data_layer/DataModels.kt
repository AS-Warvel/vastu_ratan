package com.example.project_vastuapp.data_layer

import androidx.compose.ui.graphics.vector.ImageVector
import com.google.firebase.firestore.IgnoreExtraProperties

data class DataModels (
    val furnitureType: String,
    val direction: String,
    val benefits: String,
    val avoid: String,
    val room: String,
)

@IgnoreExtraProperties
data class GeetaQuote(
    val id: Int = 0,
    val quote: String = "",
    val isFavourite: Boolean = false
)

data class VastuObject(
    val room: String,
    val furnitureType: String,
    val icon: ImageVector,
    val correctDirection: String,
    val whyThisDirection: String,
    val directionsToAvoid: String? = null
)

data class PlacementVastuObject(
    val room: String,
    val `object`: String, // 'object' is a keyword in Kotlin, so use backticks
    val recommendedDirections: List<String>,
)
