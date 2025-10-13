package com.example.project_vastuapp.datalayer

// VastuCalculatorUseCase.kt (Domain Layer)

// Adjust imports based on your package structure
import com.example.project_vastuapp.datalayer.VastuScanData
import com.example.project_vastuapp.datalayer.VastuAnalysisResult
import com.example.project_vastuapp.datalayer.DetectedObject
import android.graphics.Color // Needed for the color property

class VastuCalculatorUseCase {

    /**
     * Takes raw scan data (objects + compass) and applies all Vastu rules.
     * @return A list of objects with full Vastu analysis attached.
     */
    fun calculateVastuScore(scanData: VastuScanData): List<VastuAnalysisResult> {
        val results = mutableListOf<VastuAnalysisResult>()

        // The absolute North direction from the sensor data
        val northDirection = scanData.sensorData.compassDirection

        for (rawObject in scanData.rawObjects) {

            // 1. Determine the Vastu Zone
            val vastuZone = getVastuZoneForObject(rawObject, northDirection)

            // 2. Apply Vastu Dosh Rules
            val (score, feedback, color) = applyVastuRules(rawObject.label, vastuZone)

            results.add(
                VastuAnalysisResult(
                    detectedObject = rawObject,
                    vastuZone = vastuZone,
                    vastuScore = score,
                    vastuFeedback = feedback,
                    arOverlayColor = color
                )
            )
        }
        return results
    }

    // --- Core Vastu Logic Simulation (Requires refinement for production) ---

    private fun getVastuZoneForObject(obj: DetectedObject, northDirection: Float): String {
        /* * This function is highly simplified. A production app requires complex geometry:
        * 1. Projecting the 2D bounding box to 3D space (requires device tilt correction).
        * 2. Calculating the world angle from the room's center to the object.
        * 3. Mapping that angle (relative to North) to one of the 16 Vastu padas.
        */

        // Simplified Logic: Divide the 360 degrees into 8 main zones starting from North (0/360)
        val angle = (northDirection % 360).toInt()

        return when (angle) {
            in 337..360, in 0..22 -> "North (N)"
            in 23..67 -> "North-East (NE)"
            in 68..112 -> "East (E)"
            in 113..157 -> "South-East (SE)"
            in 158..202 -> "South (S)"
            in 203..247 -> "South-West (SW)"
            in 248..292 -> "West (W)"
            in 293..336 -> "North-West (NW)"
            else -> "Unknown"
        }
    }

    private fun applyVastuRules(label: String, zone: String): Triple<Float, String, Int> {
        // Defines the Dosh (Defect) logic based on object and location
        return when {
            // High Dosh Example (Red)
            label == "Shoe Rack" && zone.contains("NE") -> Triple(0.9f, "High Dosh. Remove immediately from NE.", Color.RED)
            label == "Mirror" && zone.contains("S") -> Triple(0.8f, "High Dosh. Mirror in the South causes anxiety.", Color.RED)
            // Moderate Dosh Example (Yellow)
            label == "Refrigerator" && zone.contains("N") -> Triple(0.6f, "Moderate Dosh. Fire element in Water zone.", Color.YELLOW)
            // Auspicious Example (Green)
            label == "Plant" && zone.contains("NE") -> Triple(0.1f, "Highly auspicious. Excellent placement.", Color.GREEN)
            label == "Bed" && zone.contains("SW") -> Triple(0.2f, "Good for stability and rest.", Color.GREEN)
            // Default/Acceptable
            else -> Triple(0.3f, "Acceptable placement.", Color.rgb(0, 150, 0)) // Dark Green
        }
    }
}