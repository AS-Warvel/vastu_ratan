package com.example.project_vastuapp.datalayer
import android.graphics.Rect
import android.graphics.Color
data class FurnitureInput(
    val furnitureType: String,
    val direction: String
)

data class FurnitureOutput (
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

data class DetectedObject(
    val label: String,            // e.g., "Bed", "Sofa", "Plant"
    val boundingBox: Rect,        // Pixel coordinates [left, top, right, bottom] on the image
    val confidence: Float,        // Model's certainty (e.g., 0.95f)
    val rotationDegrees: Int      // Image rotation needed for Vastu calculations
)

data class RawScanResult(
    val detectedObjects: List<DetectedObject>,
    val timestamp: Long = System.currentTimeMillis()
)
data class SensorData(
    val compassDirection: Float, // The direction the phone is pointing (Azimuth, 0 to 360 degrees)
    val deviceRotation: FloatArray // Array for device tilt (needed for overlay drawing)
)

data class VastuScanData(
    val rawObjects: List<DetectedObject>, // From VastuAnalyzer
    val sensorData: SensorData           // From VastuSensorManager
)

data class VastuAnalysisResult(
    val detectedObject: DetectedObject,
    val vastuZone: String,          // e.g., "North-East (NE)"
    val vastuScore: Float,          // Dosh severity: 0.0f (Auspicious) to 1.0f (High Dosh)
    val vastuFeedback: String,      // e.g., "Highly inauspicious. Remove immediately."
    val arOverlayColor: Int        // Color code for the bounding box (e.g., Color.GREEN/Color.RED)
)