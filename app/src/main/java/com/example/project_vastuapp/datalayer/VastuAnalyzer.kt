package com.example.project_vastuapp.datalayer
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.example.project_vastuapp.datalayer.DetectedObject
import com.example.project_vastuapp.datalayer.RawScanResult
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.ObjectDetector
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

// We will use a dedicated class to manage the camera's ImageAnalysis logic.
class VastuAnalyzer {

    // 1. Replaced BroadcastChannel with MutableSharedFlow
    // replay=1: Stores the latest result for new collectors.
    // onBufferOverflow=DROP_OLDEST: Drops old data if the buffer fills up,
    // ensuring we process the newest camera frame quickly.
    private val _rawScanResultsFlow = MutableSharedFlow<RawScanResult>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    // The public Flow exposed to the Repository
    val rawScanResultsFlow: SharedFlow<RawScanResult> = _rawScanResultsFlow.asSharedFlow()

    // 2. Initialize ML Kit Object Detector for live stream (no change needed here)
    private val objectDetector: ObjectDetector = ObjectDetection.getClient(
        ObjectDetectorOptions.Builder()
            .setDetectorMode(ObjectDetectorOptions.STREAM_MODE)
            .enableMultipleObjects()
            .enableClassification()
            .build()
    )

    // 3. The actual CameraX Analyzer implementation
    val analyzer = ImageAnalysis.Analyzer { imageProxy ->
        // To avoid blocking the camera thread while emitting the flow,
        // we must use a coroutine dispatcher (or runBlocking in this isolated context).
        processImageProxy(imageProxy)
    }

    @OptIn(ExperimentalGetImage::class)
    private fun processImageProxy(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage == null) {
            imageProxy.close()
            return
        }

        val inputImage = InputImage.fromMediaImage(
            mediaImage,
            imageProxy.imageInfo.rotationDegrees
        )

        // Run ML Inference
        objectDetector.process(inputImage)
            .addOnSuccessListener { detectedObjects ->
                val vastuObjects = detectedObjects.map { obj ->
                    DetectedObject(
                        label = obj.labels.firstOrNull()?.text ?: "Unknown",
                        boundingBox = obj.boundingBox,
                        confidence = obj.labels.firstOrNull()?.confidence ?: 0.0f,
                        rotationDegrees = inputImage.rotationDegrees
                    )
                }

                // 4. Emit the raw results using a non-suspending method if required,
                // or ensure you're within a coroutine scope. For an Analyzer
                // typically running on an executor, use tryEmit or a dedicated scope.
                // tryEmit is safest here as it doesn't suspend the critical camera thread.
                // Emit the raw results
                val emitted = _rawScanResultsFlow.tryEmit(RawScanResult(vastuObjects))

                if (!emitted) {
                    Log.w("VastuAnalyzer", "Failed to emit scan result - buffer full or no active collectors")
                }
            }
            .addOnFailureListener { e ->
                Log.e("VastuAnalyzer", "ML detection failed: ${e.message}")
            }
            .addOnCompleteListener {
                // 5. IMPORTANT: Close the imageProxy to release the buffer
                imageProxy.close()
            }
    }
}