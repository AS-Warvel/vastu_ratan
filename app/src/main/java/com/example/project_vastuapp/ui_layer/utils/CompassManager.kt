package com.example.project_vastuapp.ui_layer.utils

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import kotlin.math.roundToInt

class CompassManager(context: Context) : SensorEventListener {

    private val sensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private val magnetometer: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

    private val gravity = FloatArray(3)
    private val geomagnetic = FloatArray(3)
    private val rotationMatrix = FloatArray(9)
    private val orientation = FloatArray(3)

    private var currentAzimuth = 0f
    private var onDirectionChanged: ((Float, String) -> Unit)? = null

    companion object {
        private const val TAG = "CompassManager"
        private const val ALPHA = 0.15f // Smoothing factor for low-pass filter
    }

    init {
        if (accelerometer == null || magnetometer == null) {
            Log.e(TAG, "⚠️ Compass sensors not available on this device!")
        } else {
            Log.d(TAG, "✓ Compass sensors initialized")
        }
    }

    /**
     * Start listening to compass sensors
     */
    fun start() {
        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
        magnetometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
        Log.d(TAG, "Compass started")
    }

    /**
     * Stop listening to compass sensors
     */
    fun stop() {
        sensorManager.unregisterListener(this)
        Log.d(TAG, "Compass stopped")
    }

    /**
     * Set callback for when direction changes
     */
    fun setOnDirectionChangedListener(listener: (azimuth: Float, direction: String) -> Unit) {
        onDirectionChanged = listener
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return

        when (event.sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> {
                // Apply low-pass filter for smoothing
                gravity[0] = ALPHA * event.values[0] + (1 - ALPHA) * gravity[0]
                gravity[1] = ALPHA * event.values[1] + (1 - ALPHA) * gravity[1]
                gravity[2] = ALPHA * event.values[2] + (1 - ALPHA) * gravity[2]
            }
            Sensor.TYPE_MAGNETIC_FIELD -> {
                // Apply low-pass filter for smoothing
                geomagnetic[0] = ALPHA * event.values[0] + (1 - ALPHA) * geomagnetic[0]
                geomagnetic[1] = ALPHA * event.values[1] + (1 - ALPHA) * geomagnetic[1]
                geomagnetic[2] = ALPHA * event.values[2] + (1 - ALPHA) * geomagnetic[2]
            }
        }

        // Calculate orientation
        val success = SensorManager.getRotationMatrix(rotationMatrix, null, gravity, geomagnetic)
        if (success) {
            SensorManager.getOrientation(rotationMatrix, orientation)

            // Convert radians to degrees
            var azimuth = Math.toDegrees(orientation[0].toDouble()).toFloat()

            // Normalize to 0-360
            azimuth = (azimuth + 360) % 360

            // Update current azimuth
            currentAzimuth = azimuth

            // Get direction string
            val direction = getDirectionFromAzimuth(azimuth)

            // Notify listener
            onDirectionChanged?.invoke(azimuth, direction)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        when (accuracy) {
            SensorManager.SENSOR_STATUS_UNRELIABLE ->
                Log.w(TAG, "Compass accuracy: UNRELIABLE")
            SensorManager.SENSOR_STATUS_ACCURACY_LOW ->
                Log.w(TAG, "Compass accuracy: LOW")
            SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM ->
                Log.d(TAG, "Compass accuracy: MEDIUM")
            SensorManager.SENSOR_STATUS_ACCURACY_HIGH ->
                Log.d(TAG, "Compass accuracy: HIGH")
        }
    }

    /**
     * Get current azimuth in degrees (0-360)
     */
    fun getCurrentAzimuth(): Float = currentAzimuth

    /**
     * Get current direction as string
     */
    fun getCurrentDirection(): String = getDirectionFromAzimuth(currentAzimuth)

    /**
     * Convert azimuth to cardinal direction
     */
    fun getDirectionFromAzimuth(azimuth: Float): String {
        return when (azimuth.roundToInt()) {
            in 0..22, in 338..360 -> "North"
            in 23..67 -> "North-East"
            in 68..112 -> "East"
            in 113..157 -> "South-East"
            in 158..202 -> "South"
            in 203..247 -> "South-West"
            in 248..292 -> "West"
            in 293..337 -> "North-West"
            else -> "Unknown"
        }
    }

    /**
     * Get short direction (N, NE, E, SE, S, SW, W, NW)
     */
    fun getShortDirection(): String {
        return when (getDirectionFromAzimuth(currentAzimuth)) {
            "North" -> "N"
            "North-East" -> "NE"
            "East" -> "E"
            "South-East" -> "SE"
            "South" -> "S"
            "South-West" -> "SW"
            "West" -> "W"
            "North-West" -> "NW"
            else -> "?"
        }
    }

    /**
     * Check if sensors are available
     */
    fun isAvailable(): Boolean {
        return accelerometer != null && magnetometer != null
    }
}