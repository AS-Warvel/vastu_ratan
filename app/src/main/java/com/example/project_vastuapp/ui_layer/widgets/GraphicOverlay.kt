package com.example.project_vastuapp.ui_layer.widgets

// GraphicOverlay.kt (UI Layer / Widget)

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.example.project_vastuapp.datalayer.VastuAnalysisResult
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.drawscope.Stroke

// Helper function to convert android.graphics.Color to Compose Color
private fun Int.toComposeColor() = Color(this)

@Composable
fun GraphicOverlay(
    results: List<VastuAnalysisResult>
) {
    val density = LocalDensity.current

    val textSizeDp = 18.dp
    val textOffsetDp = 10.dp

    // Initialize Android Paint for drawing text on the native Canvas
    val textPaint = remember {
        android.graphics.Paint().apply {
            textAlign = android.graphics.Paint.Align.LEFT
        }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {

        with(density) {
            // Apply text size inside the density scope
            textPaint.textSize = textSizeDp.toPx()
        }

        results.forEach { result ->
            val rect = result.detectedObject.boundingBox
            val color = result.arOverlayColor.toComposeColor()
            val text = "${result.vastuZone}: ${result.vastuFeedback}"

            // 1. Draw the Bounding Box (Rectangle)
            drawRect(
                color = color,
                topLeft = Offset(rect.left.toFloat(), rect.top.toFloat()),
                size = Size(rect.width().toFloat(), rect.height().toFloat()),
                style = Stroke(width = 4f)
            )

            // 2. Draw the Vastu Feedback Text
            drawContext.canvas.nativeCanvas.drawText(
                text,
                rect.left.toFloat(),
                rect.top.toFloat() - with(density) { textOffsetDp.toPx() }, // Position text above
                textPaint.apply {
                    this.color = result.arOverlayColor // Use Vastu color for the text
                }
            )
        }
    }
}