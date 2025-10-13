package com.example.project_vastuapp.ui_layer.screens

import AddFurnitureObjectDialogBox
import android.util.Log
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LifecycleOwner
import com.example.project_vastuapp.ui_layer.widgets.*
import com.example.project_vastuapp.viewmodel.VastuAppViewModel

/**
 * Home Screen - Main screen with Check Vastu and Camera Scan buttons
 */
@Composable
fun HomeScreen(
    onCheckVastu: () -> Unit,
    onCameraScan: () -> Unit
) {
    ModeSelectionScreen(
        onCheckVastu = onCheckVastu,
        onCameraScan = onCameraScan
    )
}

/**
 * Manual Input Screen - Removed (No longer needed)
 * Check Vastu dialog now opens directly from home screen
 */

/**
 * Camera Scan Screen - Screen for live camera scanning
 */
@Composable
fun CameraScanScreen(
    viewModel: VastuAppViewModel,
    lifecycleOwner: LifecycleOwner,
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val vastuScanState by viewModel.vastuScanState.collectAsState()

    // Initialize camera preview
    val previewView = remember {
        PreviewView(context).apply {
            scaleType = PreviewView.ScaleType.FILL_CENTER
        }
    }

    // Start camera scan
    DisposableEffect(lifecycleOwner, previewView) {
        viewModel.startVastuScan(lifecycleOwner, previewView)
        onDispose {
            // Cleanup if needed
        }
    }

    // Camera screen layout
    Box(modifier = Modifier.fillMaxSize()) {
        // Camera feed layer
        CameraFeedView(previewView = previewView)

        // Overlay layer
        CameraScanOverlay(vastuScanState = vastuScanState)
    }
}