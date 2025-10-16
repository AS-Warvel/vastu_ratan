package com.example.project_vastuapp.ui_layer.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.widget.Toast
import androidx.navigation.NavController
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun VastuCompassScreen(navController: NavController) {
    // State to hold the current rotation angle of the compass
    var rotationAngle by remember { mutableStateOf(0f) }
    // State to hold the angle at the start of a drag gesture
    var dragStartAngle by remember { mutableStateOf(0f) }
    // Context for showing toast messages
    val context = LocalContext.current

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFFFF8E1) // A soft, parchment-like background color
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Set Plot Direction",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF5D4037)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Drag the compass to align with the North direction of your plot.",
                fontSize = 16.sp,
                color = Color(0xFF795548),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))

            // The main interactive area containing the plot and compass
            Box(
                modifier = Modifier
                    .size(320.dp),
                contentAlignment = Alignment.Center
            ) {
                // The square plot representation
                Box(
                    modifier = Modifier
                        .size(220.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFD2B48C)) // Tan color for the plot
                )

                // The rotatable compass
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDragStart = { offset ->
                                    // Calculate the angle of the initial touch point
                                    val centerX = size.width / 2f
                                    val centerY = size.height / 2f
                                    val dx = offset.x - centerX
                                    val dy = offset.y - centerY
                                    dragStartAngle = atan2(dy, dx) * (180f / PI).toFloat()
                                },
                                onDrag = { change, _ ->
                                    // Calculate the angle of the current drag point
                                    val centerX = size.width / 2f
                                    val centerY = size.height / 2f
                                    val dx = change.position.x - centerX
                                    val dy = change.position.y - centerY
                                    val currentAngle = atan2(dy, dx) * (180f / PI).toFloat()

                                    // Calculate the change in angle and update the rotation
                                    val angleDifference = currentAngle - dragStartAngle
                                    rotationAngle += angleDifference
                                    dragStartAngle = currentAngle
                                }
                            )
                        }
                ) {
                    // Custom Canvas to draw the compass
                    Compass(rotationAngle = rotationAngle)
                }

                // A static pointer indicating the 'top' of the plot
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .size(24.dp, 36.dp),
                    contentAlignment = Alignment.Center
                ){
                    Text("▲", color = Color.Red, fontSize = 24.sp)
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Button to confirm the selected direction
            Button(
                onClick = {
                    // Normalize the angle to be between 0 and 360
                    val finalAngle = (rotationAngle % 360 + 360) % 360
                    Toast.makeText(
                        context,
                        "Direction confirmed: ${String.format("%.1f", finalAngle)}°",
                        Toast.LENGTH_SHORT
                    ).show()
                    navController.navigate("House/$finalAngle")

                },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF9800) // Saffron/Orange color
                ),
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                Text(
                    text = "Confirm Direction",
                    fontSize = 18.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}

@Composable
fun Compass(rotationAngle: Float) {
    // Native paint object for drawing text on canvas
    val textPaint = remember {
        Paint().asFrameworkPaint().apply {
            isAntiAlias = true
            textSize = 50f
            color = android.graphics.Color.BLACK
            textAlign = android.graphics.Paint.Align.CENTER
            typeface = android.graphics.Typeface.create(android.graphics.Typeface.DEFAULT, android.graphics.Typeface.BOLD)
        }
    }

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            // Apply the rotation transformation
            .graphicsLayer {
                rotationZ = rotationAngle
            }
    ) {
        val radius = size.minDimension / 2f - 20f
        val centerX = center.x
        val centerY = center.y

        // Draw the main compass ring
        drawCircle(
            color = Color.Black,
            radius = radius,
            style = Stroke(width = 4.dp.toPx())
        )
        drawCircle(
            color = Color.White.copy(alpha = 0.7f),
            radius = radius,
        )

        // Draw degree markers
        for (i in 0 until 360 step 2) {
            val angleRad = Math.toRadians(i.toDouble()).toFloat()
            val isMajorTick = i % 30 == 0

            val startRadius = if (isMajorTick) radius - 30f else radius - 15f
            val endRadius = radius

            val startX = centerX + startRadius * cos(angleRad)
            val startY = centerY + startRadius * sin(angleRad)
            val endX = centerX + endRadius * cos(angleRad)
            val endY = centerY + endRadius * sin(angleRad)

            drawLine(
                color = Color.Black,
                start = Offset(startX, startY),
                end = Offset(endX, endY),
                strokeWidth = if (isMajorTick) 4f else 2f
            )
        }

        // Draw cardinal direction text (N, E, S, W)
        drawIntoCanvas { canvas ->
            val directions = mapOf(
                "N" to -90f,
                "E" to 0f,
                "S" to 90f,
                "W" to 180f
            )

            directions.forEach { (text, angle) ->
                val angleRad = Math.toRadians(angle.toDouble()).toFloat()
                val textRadius = radius - 80f
                val x = centerX + textRadius * cos(angleRad)
                val y = centerY + textRadius * sin(angleRad)

                // Counter-rotate the canvas for each text element so it remains upright
                canvas.nativeCanvas.save()
//                canvas.nativeCanvas.rotate(90f + angle, x, y) // Rotate to orient correctly
//                canvas.nativeCanvas.rotate(-rotationAngle, x, y) // Counter-rotate the main rotation
                canvas.nativeCanvas.drawText(text, x, y, textPaint)
                canvas.nativeCanvas.restore()
            }
        }
    }
}
//
//@Preview(showBackground = true)
//@Composable
//fun VastuCompassScreenPreview() {
//    MaterialTheme {
//        VastuCompassScreen()
//    }
//}
