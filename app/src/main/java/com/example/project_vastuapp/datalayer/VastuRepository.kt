package com.example.project_vastuapp.datalayer

import android.content.Context
import androidx.camera.core.*
import androidx.camera.view.PreviewView
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import android.util.Log
import java.lang.Exception

// Assuming these classes exist in your datalayer package:
// data class FurnitureRule(...)
// data class VastuScanData(...)
// class VastuAnalyzer
// class VastuSensorManager

class VastuRepository(
    private val context: Context, // Required for CameraX/Sensors
    private val db: FirebaseFirestore, // Existing dependency
    // NEW DEPENDENCIES for Real-Time Scan:
    private val vastuAnalyzer: VastuAnalyzer,
    private val vastuSensorManager: VastuSensorManager
) {

    // The existing interface for manual Vastu checks (UNCHANGED)
    interface FurnitureRuleCallback {
        fun onSuccess(rule: FurnitureRule?)
        fun onError(exception: Exception)
    }

    // The existing Firebase function (UNCHANGED)
    fun getFurnitureRule(furnitureType: String, callback: FurnitureRuleCallback) {
        val typeId = furnitureType.lowercase()

        db.collection("furniture_rules")
            .document(typeId)
            .get()
            .addOnSuccessListener { document ->
                val rule = document.toObject<FurnitureRule>()
                callback.onSuccess(rule)
            }
            .addOnFailureListener { exception ->
                callback.onError(exception)
            }
    }

    // =================================================================
    // NEW REAL-TIME CAMERA / ML FUNCTIONS
    // =================================================================

    /**
     * Combines the real-time ML object detection flow and compass sensor data flow.
     * This provides the single source of truth for raw Vastu input to the Domain Layer.
     */
    fun getRealtimeVastuScanResults(): Flow<VastuScanData> {
        // Use the 'combine' operator to synchronize the two independent real-time streams
        return vastuAnalyzer.rawScanResultsFlow
            .combine(vastuSensorManager.getSensorDataFlow()) { rawResult, sensorData ->
                VastuScanData(
                    rawObjects = rawResult.detectedObjects,
                    sensorData = sensorData
                )
            }
    }

    /**
     * Binds the necessary CameraX Use Cases (Preview and ImageAnalysis) to the UI lifecycle.
     * This function is called by the ViewModel when the camera screen is opened.
     */
    fun bindCameraToLifecycle(lifecycleOwner: LifecycleOwner, previewView: PreviewView) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            // 1. Preview Use Case: Displays camera feed on the PreviewView
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            // 2. ImageAnalysis Use Case: Feeds frames to the VastuAnalyzer
            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            // Set the analyzer defined in VastuAnalyzer.kt to process frames
            // We run it on the main executor for simplicity in context access,
            // but the VastuAnalyzer handles threading for ML internally.
            imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(context), vastuAnalyzer.analyzer)

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner, cameraSelector, preview, imageAnalysis
                )
            } catch (exc: Exception) {
                Log.e("VastuRepo", "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(context))
    }
}