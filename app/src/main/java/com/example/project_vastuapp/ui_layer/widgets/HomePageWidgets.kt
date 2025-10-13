package com.example.project_vastuapp.ui_layer.widgets

import android.content.Context
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import com.example.project_vastuapp.R
import com.example.project_vastuapp.ui.theme.background
import com.example.project_vastuapp.ui.theme.black
import com.example.project_vastuapp.ui.theme.buttonContainer
import com.example.project_vastuapp.viewmodel.VastuScanState

// ==========================================
// MODE SELECTION SCREEN
// ==========================================

/**
 * Mode Selection Screen - Main home screen with Check Vastu and Camera Scan options
 */
@Composable
fun ModeSelectionScreen(
    onCheckVastu: () -> Unit,
    onCameraScan: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        VastuLogo(size = 150)

        Spacer(modifier = Modifier.height(60.dp))

        Text(
            text = "Welcome to Vastu Ratan",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Choose your analysis method",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(60.dp))

        // Check Your Vastu Button (Manual Input)
        Button(
            onClick = onCheckVastu,
            modifier = Modifier
                .width(300.dp)
                .height(100.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = buttonContainer,
                contentColor = black
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = "Check Your Vastu",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Camera Scan Button
        Button(
            onClick = onCameraScan,
            modifier = Modifier
                .width(300.dp)
                .height(100.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4CAF50),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = "Camera Scan",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// ==========================================
// BASIC REUSABLE WIDGETS
// ==========================================

/**
 * Check Vastu Button - Main action button
 */
@Composable
fun CheckVastuButton(
    enableAddFurnitureScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = { enableAddFurnitureScreen() },
        modifier = modifier
            .height(100.dp)
            .width(300.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonContainer,
            contentColor = black,
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = "Check Your Vastu",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * Vastu Logo Icon
 */
@Composable
fun VastuLogo(
    modifier: Modifier = Modifier,
    size: Int = 120
) {
    Icon(
        painter = painterResource(id = R.drawable.vastu_ratan_logo),
        tint = Color(0xFFF68B6C),
        contentDescription = "Vastu Ratan App Logo",
        modifier = modifier.size(size.dp)
    )
}

/**
 * Welcome Banner Card
 */
@Composable
fun WelcomeBanner(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF00BCD4))
                .padding(24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Welcome to Vastu Ratan",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// ==========================================
// SCREEN CONTENT COMPOSABLES
// ==========================================

/**
 * Manual Input Screen Layout
 */
@Composable
fun ManualInputContent(
    onCheckVastu: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        VastuLogo(size = 150)

        Spacer(modifier = Modifier.height(80.dp))

        Text(
            text = "Manual Vastu Analysis",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Add your furniture details to check Vastu compliance",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(60.dp))

        CheckVastuButton(enableAddFurnitureScreen = onCheckVastu)
    }
}

/**
 * Camera Feed View
 */
@Composable
fun CameraFeedView(
    previewView: PreviewView,
    modifier: Modifier = Modifier
) {
    AndroidView(
        factory = { previewView },
        modifier = modifier.fillMaxSize()
    )
}

/**
 * Camera Scan Overlay - Shows detection results
 */
@Composable
fun CameraScanOverlay(
    vastuScanState: VastuScanState,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        when (vastuScanState) {
            is VastuScanState.Scanning -> {
                // Detection overlay
                GraphicOverlay(results = vastuScanState.results)

                // Status at bottom
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 16.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    CameraScanStatusBox(objectCount = vastuScanState.results.size)
                }
            }

            VastuScanState.Idle -> {
                CameraLoadingState(message = "Initializing Scanner...")
            }

            is VastuScanState.Error -> {
                CameraErrorState(message = "Camera Error: ${vastuScanState.message}")
            }
        }
    }
}

/**
 * Status Box for Camera Scan
 */
@Composable
fun CameraScanStatusBox(
    objectCount: Int,
    modifier: Modifier = Modifier
) {
    Text(
        text = "Vastu Scan: $objectCount objects analyzed live",
        color = Color.White,
        fontSize = 16.sp,
        modifier = modifier
            .background(
                Color.Black.copy(alpha = 0.6f),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

/**
 * Loading State for Camera
 */
@Composable
fun CameraLoadingState(
    message: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(50.dp),
            color = Color(0xFFF68B6C)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = message,
            color = Color.White,
            fontSize = 16.sp
        )
    }
}

/**
 * Error State for Camera
 */
@Composable
fun CameraErrorState(
    message: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFFEBEE)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Error",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.Red,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = message,
                    color = Color(0xFFD32F2F),
                    fontSize = 14.sp
                )
            }
        }
    }
}