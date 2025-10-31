package com.example.project_vastuapp.ui_layer.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project_vastuapp.R // Import your R file
import kotlinx.coroutines.delay

/**
 * A splash screen that shows a rotating logo and fading-in title,
 * then navigates to the home screen after a delay.
 *
 * @param onTimeout A lambda function to be called when the splash screen duration is over.
 * This is where you should trigger your navigation to the home page.
 */
@Composable
fun SplashScreen(onTimeout: () -> Unit) {

    var startAnimation by remember { mutableStateOf(false) }

    // Animation for rotation (0 degrees to 360 degrees)
    val rotationAngle by animateFloatAsState(
        targetValue = if (startAnimation) 60f else 0f,
        animationSpec = tween(
            durationMillis = 2000, // 2 seconds for one full rotation
            easing = LinearEasing
        ),
        label = "rotation"
    )

    // Animation for alpha (fade-in)
    val alpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 1500, // 1.5 seconds to fade in
//            delayMillis = 500      // Start fading in after 0.5 seconds
        ),
        label = "alpha"
    )

    // Trigger the animation and the timeout navigation
    LaunchedEffect(key1 = true) {
        startAnimation = true // Start the animations
        delay(3000L) // Wait for 3 seconds (adjust as needed)
        onTimeout() // Call the navigation function
    }

    // UI Layout
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background // Use your app's background color
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Your Image Asset
                Image(
                    painter = painterResource(id = R.drawable.vastu_ratan_logo), // <-- REPLACE this with your actual image asset
                    contentDescription = "App Logo",
                    modifier = Modifier
                        .size(250.dp) // Adjust size as needed
                        .rotate(rotationAngle) // Apply rotation animation
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Your App Title
                Text(
                    text = "Vastu Ratan",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black, // Adjust color as needed
                    modifier = Modifier.alpha(alpha) // Apply fade-in animation
                )
            }
        }
    }
}

//// A preview function to see the splash screen in Android Studio's design view
//@Preview(showBackground = true)
//@Composable
//fun SplashScreenPreview() {
//    // You can wrap this in your app's theme if you have one
//    SplashScreen(onTimeout = {}) // Pass an empty lambda for the preview
//}
//
