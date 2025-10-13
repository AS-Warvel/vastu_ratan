package com.example.project_vastuapp.datalayer

// VastuSensorManager.kt (Data Layer)

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
// Adjust import for SensorData based on your package structure
import com.example.project_vastuapp.datalayer.SensorData
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class VastuSensorManager(private val context: Context) {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    private val accelerometerReading = FloatArray(3)
    private val magnetometerReading = FloatArray(3)
    private val rotationMatrix = FloatArray(9)
    private val orientationAngles = FloatArray(3)

    /**
     * Exposes a real-time stream of the device's orientation data as a Kotlin Flow.
     */
    fun getSensorDataFlow(): Flow<SensorData> = callbackFlow {

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event == null) return

                when (event.sensor.type) {
                    Sensor.TYPE_ACCELEROMETER ->
                        System.arraycopy(event.values, 0, accelerometerReading, 0, 3)

                    Sensor.TYPE_MAGNETIC_FIELD ->
                        System.arraycopy(event.values, 0, magnetometerReading, 0, 3)

                    else -> return
                }

                // Only proceed if we have fresh data from both sensors
                if (accelerometerReading.isNotEmpty() && magnetometerReading.isNotEmpty()) {

                    // Compute Rotation Matrix (R)
                    SensorManager.getRotationMatrix(
                        rotationMatrix,
                        null,
                        accelerometerReading,
                        magnetometerReading
                    )

                    // Compute Orientation Angles (Azimuth/Compass Direction)
                    SensorManager.getOrientation(rotationMatrix, orientationAngles)

                    // Azimuth is the compass direction (radians). Convert to Degrees (0-360)
                    val azimuthInDegrees = (Math.toDegrees(orientationAngles[0].toDouble()) + 360) % 360

                    // Send the result to the flow
                    trySend(
                        SensorData(
                            compassDirection = azimuthInDegrees.toFloat(),
                            deviceRotation = orientationAngles
                        )
                    ).isSuccess
                }
            }

            // Correct function name, as fixed in our earlier discussion
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                // Optional: Handle sensor accuracy changes
            }
        }

        // Register the listeners for required sensors
        sensorManager.registerListener(
            listener,
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_GAME
        )
        sensorManager.registerListener(
            listener,
            sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
            SensorManager.SENSOR_DELAY_GAME
        )

        // Stop listening when the flow is closed (lifecycle awareness)
        awaitClose {
            sensorManager.unregisterListener(listener)
        }
    }
}