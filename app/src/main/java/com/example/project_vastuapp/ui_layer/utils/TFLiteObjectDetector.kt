package com.example.project_vastuapp.ui_layer.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.RectF
import android.util.Log
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.vision.detector.Detection
import org.tensorflow.lite.task.vision.detector.ObjectDetector

data class DetectedObject(
    val label: String,          // "bottle", "toilet", "chair", etc.
    val confidence: Float,      // 0.0 to 1.0
    val boundingBox: RectF      // Position in image
)

class TFLiteObjectDetector(context: Context) {

    private var detector: ObjectDetector? = null

    companion object {
        private const val TAG = "TFLiteDetector"
        private const val MODEL_FILE = "detect.tflite"
        private const val CONFIDENCE_THRESHOLD = 0.5f
        private const val MAX_RESULTS = 10
    }

    init {
        try {
            val options = ObjectDetector.ObjectDetectorOptions.builder()
                .setMaxResults(MAX_RESULTS)
                .setScoreThreshold(CONFIDENCE_THRESHOLD)
                .build()

            detector = ObjectDetector.createFromFileAndOptions(
                context,
                MODEL_FILE,
                options
            )

            Log.d(TAG, "TensorFlow Lite model loaded successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Error loading TensorFlow model", e)
        }
    }

    fun detectObjects(bitmap: Bitmap): List<DetectedObject> {
        if (detector == null) {
            Log.e(TAG, "Detector not initialized")
            return emptyList()
        }

        return try {
            // Convert bitmap to TensorImage
            val tensorImage = TensorImage.fromBitmap(bitmap)

            // Run detection
            val results = detector?.detect(tensorImage) ?: emptyList()

            // Convert to our DetectedObject format
            val detectedObjects = results.map { detection ->
                DetectedObject(
                    label = detection.categories.firstOrNull()?.label ?: "Unknown",
                    confidence = detection.categories.firstOrNull()?.score ?: 0f,
                    boundingBox = detection.boundingBox
                )
            }

            Log.d(TAG, "Detected ${detectedObjects.size} objects")
            detectedObjects.forEach { obj ->
                Log.d(TAG, "  - ${obj.label} (${(obj.confidence * 100).toInt()}%)")
            }

            detectedObjects

        } catch (e: Exception) {
            Log.e(TAG, "Detection failed", e)
            emptyList()
        }
    }

    fun close() {
        detector?.close()
        detector = null
    }
}