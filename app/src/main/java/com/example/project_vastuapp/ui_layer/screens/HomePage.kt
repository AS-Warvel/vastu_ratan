package com.example.project_vastuapp.ui_layer.screens

import AddFurnitureObjectDialogBox
import android.Manifest
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.project_vastuapp.ui_layer.widgets.reusable_components.AppBar
import com.example.project_vastuapp.viewmodel.VastuAppViewModel
import com.example.project_vastuapp.viewmodel.VastuModeState

/**
 * HomePage - Entry point with navigation logic only
 * Simplified flow: Check Vastu opens dialog directly, no intermediate screen
 */
@ExperimentalMaterial3Api
@Composable
fun HomePage(
    myViewModel: VastuAppViewModel = viewModel()
) {
    val currentMode by myViewModel.currentMode.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    // Dialog state for Check Vastu
    var showAddFurnitureDialog by remember { mutableStateOf(false) }

    // Camera permission launcher
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                myViewModel.setMode(VastuModeState.CameraScan)
            } else {
                Log.e("HomePage", "Camera permission denied.")
                myViewModel.setMode(VastuModeState.SelectMode)
            }
        }
    )

    // Request camera permission when needed
    LaunchedEffect(currentMode) {
        if (currentMode is VastuModeState.CameraScan) {
            val permissionStatus = androidx.core.content.ContextCompat.checkSelfPermission(
                context, Manifest.permission.CAMERA
            )

            if (permissionStatus != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    // Main navigation structure
    Column(modifier = Modifier.fillMaxSize()) {
        AppBar(showActions = true)

        when (currentMode) {
            VastuModeState.SelectMode, VastuModeState.ManualInput -> {
                // Home screen with both buttons
                HomeScreen(
                    onCheckVastu = {
                        showAddFurnitureDialog = true
                    },
                    onCameraScan = {
                        val permissionStatus = androidx.core.content.ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.CAMERA
                        )
                        if (permissionStatus == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                            myViewModel.setMode(VastuModeState.CameraScan)
                        } else {
                            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    }
                )
            }

            VastuModeState.CameraScan -> {
                // Camera scan screen
                CameraScanScreen(
                    viewModel = myViewModel,
                    lifecycleOwner = lifecycleOwner,
                    onBack = myViewModel::resetMode
                )
            }
        }
    }

    // Show furniture dialog when Check Vastu is clicked (outside Column)
    if (showAddFurnitureDialog) {
        AddFurnitureObjectDialogBox(
            sendFurnitureObjects = { furnitureInputObjects ->
                Log.d("HomePage", "Received ${furnitureInputObjects.size} furniture objects")
                showAddFurnitureDialog = false
                myViewModel.setFurnitureInputObjects(furnitureInputObjects)
                myViewModel.evaluateFurnitureList()
            },
            cancelOperation = {
                showAddFurnitureDialog = false
            }
        )
    }
}