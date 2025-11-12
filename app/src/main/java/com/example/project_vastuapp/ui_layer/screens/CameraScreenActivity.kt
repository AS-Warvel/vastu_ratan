package com.example.project_vastuapp.ui_layer.screens

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.project_vastuapp.R
import com.example.project_vastuapp.ui_layer.utils.TFLiteObjectDetector
import com.example.project_vastuapp.ui_layer.utils.DetectedObject
import com.example.project_vastuapp.ui_layer.utils.CompassManager
import com.example.project_vastuapp.datalayer.FurnitureInput
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.nio.ByteBuffer

class CameraScreenActivity : AppCompatActivity() {

    private lateinit var previewView: PreviewView
    private lateinit var btnStartCamera: Button
    private lateinit var btnCapture: Button
    private lateinit var btnStopCamera: Button
    private lateinit var btnFinish: Button
    private lateinit var tvStatus: TextView
    private lateinit var tvCompass: TextView

    private var cameraProvider: ProcessCameraProvider? = null
    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService

    private lateinit var objectDetector: TFLiteObjectDetector
    private lateinit var compassManager: CompassManager

    private var capturedBitmap: Bitmap? = null
    private var detectedObjects: List<DetectedObject> = emptyList()
    private var currentDirection: String = "North"
    private var currentAzimuth: Float = 0f

    private val furnitureInputList = mutableListOf<FurnitureInput>()

    companion object {
        private const val TAG = "CameraScreen"
        private const val CAMERA_PERMISSION_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_screen)

        previewView = findViewById(R.id.previewView)
        btnStartCamera = findViewById(R.id.btnStartCamera)
        btnCapture = findViewById(R.id.btnCapture)
        btnStopCamera = findViewById(R.id.btnStopCamera)
        btnFinish = findViewById(R.id.btnFinish)
        tvStatus = findViewById(R.id.tvStatus)
        tvCompass = findViewById(R.id.tvCompass)

        cameraExecutor = Executors.newSingleThreadExecutor()

        btnStartCamera.isEnabled = true
        btnCapture.isEnabled = false
        btnStopCamera.isEnabled = false
        btnFinish.isEnabled = false

        // Initialize TFLite detector
        try {
            objectDetector = TFLiteObjectDetector(this)
            Log.d(TAG, "✅ TFLite Object Detector initialized")
            updateStatus("Ready! Click 'Start Camera' to begin")
        } catch (e: Exception) {
            Log.e(TAG, "❌ Failed to initialize detector", e)
            Toast.makeText(this, "Failed to load detection model: ${e.message}", Toast.LENGTH_LONG).show()
            updateStatus("Error: Model loading failed")
            finish()
            return
        }

        // Initialize Compass
        compassManager = CompassManager(this)
        setupCompass()

        btnStartCamera.setOnClickListener {
            Log.d(TAG, "Start Camera clicked")
            checkCameraPermission()
        }

        btnCapture.setOnClickListener {
            Log.d(TAG, "Capture clicked")
            capturePhoto()
        }

        btnStopCamera.setOnClickListener {
            Log.d(TAG, "Stop Camera clicked")
            stopCamera()
        }

        btnFinish.setOnClickListener {
            Log.d(TAG, "Finish clicked")
            finishAndSendResults()
        }
    }

    private fun setupCompass() {
        if (!compassManager.isAvailable()) {
            Toast.makeText(this, "Compass not available", Toast.LENGTH_LONG).show()
            tvCompass.text = "🧭 Compass unavailable"
            return
        }

        compassManager.setOnDirectionChangedListener { azimuth, direction ->
            runOnUiThread {
                currentAzimuth = azimuth
                currentDirection = direction
                updateCompassDisplay(azimuth, direction)
            }
        }

        compassManager.start()
        Log.d(TAG, "Compass started")
    }

    private fun updateCompassDisplay(azimuth: Float, direction: String) {
        tvCompass.text = "🧭 $direction (${azimuth.toInt()}°)"
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera()
            } else {
                Toast.makeText(this, "Camera permission required", Toast.LENGTH_SHORT).show()
                updateStatus("Permission denied")
            }
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            try {
                cameraProvider = cameraProviderFuture.get()

                val preview = Preview.Builder()
                    .build()
                    .also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }

                imageCapture = ImageCapture.Builder()
                    .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                    .build()

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                cameraProvider?.unbindAll()

                cameraProvider?.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )

                btnStartCamera.isEnabled = false
                btnCapture.isEnabled = true
                btnStopCamera.isEnabled = true
                updateStatus("Camera ready - Point at furniture\nFacing: $currentDirection")

                Toast.makeText(this, "Camera started", Toast.LENGTH_SHORT).show()

            } catch (e: Exception) {
                Log.e(TAG, "Camera start failed", e)
                Toast.makeText(this, "Camera failed: ${e.message}", Toast.LENGTH_SHORT).show()
                updateStatus("Camera failed")
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun capturePhoto() {
        val imageCapture = imageCapture ?: run {
            Log.e(TAG, "ImageCapture is null!")
            Toast.makeText(this, "Camera not ready", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d(TAG, "Starting photo capture...")
        updateStatus("Capturing...\nDirection: $currentDirection")

        imageCapture.takePicture(
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    Log.d(TAG, "✅ Image captured successfully")

                    capturedBitmap = imageProxyToBitmap(image)
                    image.close()

                    stopCamera()

                    updateStatus("Analyzing furniture...\nDirection: $currentDirection")
                    Toast.makeText(
                        this@CameraScreenActivity,
                        "Detecting furniture...",
                        Toast.LENGTH_SHORT
                    ).show()

                    // Run detection on background thread
                    cameraExecutor.execute {
                        capturedBitmap?.let { bitmap ->
                            try {
                                Log.d(TAG, "Running object detection...")
                                detectedObjects = objectDetector.detectObjects(bitmap)
                                Log.d(TAG, "✅ Detection complete: ${detectedObjects.size} objects found")

                                runOnUiThread {
                                    showDetectionResults()
                                }
                            } catch (e: Exception) {
                                Log.e(TAG, "❌ Detection failed", e)
                                runOnUiThread {
                                    Toast.makeText(
                                        this@CameraScreenActivity,
                                        "Detection failed: ${e.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    updateStatus("Detection failed - Try again\nClick 'Start Camera' to retry")
                                    btnStartCamera.isEnabled = true
                                }
                            }
                        }
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e(TAG, "❌ Capture failed", exception)
                    Toast.makeText(
                        this@CameraScreenActivity,
                        "Capture failed: ${exception.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateStatus("Capture failed - Try again")
                    btnStartCamera.isEnabled = true
                }
            }
        )
    }

    private fun showDetectionResults() {
        Log.d(TAG, "Showing detection results: ${detectedObjects.size} objects")

        if (detectedObjects.isEmpty()) {
            updateStatus("No furniture detected\nTry pointing at furniture\nCaptured facing: $currentDirection\n\nClick 'Start Camera' to try again")
            Toast.makeText(this, "No furniture detected", Toast.LENGTH_SHORT).show()
            btnStartCamera.isEnabled = true
            btnFinish.isEnabled = furnitureInputList.isNotEmpty()
            return
        }

        val newFurnitureInputs = detectedObjects.map { obj ->
            FurnitureInput(
                furnitureType = obj.label,
                direction = currentDirection
            )
        }

        furnitureInputList.addAll(newFurnitureInputs)

        val resultText = buildString {
            append("✓ Detected ${detectedObjects.size} item(s):\n")
            append("Direction: $currentDirection (${currentAzimuth.toInt()}°)\n\n")
            detectedObjects.forEach { obj ->
                append("• ${obj.label}\n")
                append("  ${(obj.confidence * 100).toInt()}% confident\n")
                append("  Facing: $currentDirection\n\n")
            }
            append("━━━━━━━━━━━━━━━━━━\n")
            append("Total captured: ${furnitureInputList.size}\n")
            append("\nOptions:\n")
            append("• Capture More: Start camera\n")
            append("• Done: Click 'Finish & Analyze'")
        }

        updateStatus(resultText)

        Toast.makeText(
            this,
            "Added: ${detectedObjects.joinToString(", ") { it.label }} - $currentDirection",
            Toast.LENGTH_LONG
        ).show()

        btnStartCamera.isEnabled = true
        btnFinish.isEnabled = true
    }

    private fun finishAndSendResults() {
        if (furnitureInputList.isEmpty()) {
            Toast.makeText(this, "No furniture detected yet. Capture some items first!", Toast.LENGTH_LONG).show()
            return
        }

        Log.d(TAG, "📤 Sending ${furnitureInputList.size} items for Vastu analysis")
        furnitureInputList.forEach {
            Log.d(TAG, "  - ${it.furnitureType}: ${it.direction}")
        }

        val resultIntent = Intent()
        resultIntent.putParcelableArrayListExtra("furniture_list", ArrayList(furnitureInputList))
        setResult(Activity.RESULT_OK, resultIntent)
        Toast.makeText(this, "Sending ${furnitureInputList.size} items for analysis", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun imageProxyToBitmap(image: ImageProxy): Bitmap {
        val buffer: ByteBuffer = image.planes[0].buffer
        val bytes = ByteArray(buffer.remaining())
        buffer.get(bytes)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    private fun stopCamera() {
        Log.d(TAG, "Stopping camera...")
        cameraProvider?.unbindAll()
        imageCapture = null

        btnStartCamera.isEnabled = true
        btnCapture.isEnabled = false
        btnStopCamera.isEnabled = false

        if (detectedObjects.isEmpty() && furnitureInputList.isEmpty()) {
            updateStatus("Camera stopped\nClick 'Start Camera' to begin")
        }

        Log.d(TAG, "Camera stopped. Start button enabled.")
    }

    private fun updateStatus(message: String) {
        tvStatus.text = message
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::compassManager.isInitialized) compassManager.stop()
        if (::objectDetector.isInitialized) objectDetector.close()
        cameraExecutor.shutdown()
        capturedBitmap?.recycle()
    }

    override fun onPause() {
        super.onPause()
        if (::compassManager.isInitialized) compassManager.stop()
    }

    override fun onResume() {
        super.onResume()
        if (::compassManager.isInitialized && compassManager.isAvailable()) {
            compassManager.start()
        }
    }
}
