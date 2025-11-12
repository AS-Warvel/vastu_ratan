package com.example.project_vastuapp.ui_layer.screens

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.project_vastuapp.datalayer.FurnitureInput
import com.example.project_vastuapp.ui_layer.widgets.reusable_components.AppBar
import com.example.project_vastuapp.viewmodel.VastuAppViewModel

@ExperimentalMaterial3Api
@Composable
fun HomePage(
    navController: NavController,
    myViewModel: VastuAppViewModel
) {
    val context = LocalContext.current

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val furnitureList =
                result.data?.getParcelableArrayListExtra<FurnitureInput>("furniture_list")
            if (!furnitureList.isNullOrEmpty()) {
                myViewModel.setFurnitureInputObjects(furnitureList)
                myViewModel.evaluateFurnitureList()
                // ✅ Navigate to result screen after evaluation
                navController.navigate("ResultScreen")
            }
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                val intent = Intent(context, CameraScreenActivity::class.java)
                cameraLauncher.launch(intent)
            } else {
                Log.e("HomePage", "Camera permission denied.")
            }
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        AppBar(showActions = true)
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val permissionStatus = androidx.core.content.ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CAMERA
                )
                if (permissionStatus == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                    val intent = Intent(context, CameraScreenActivity::class.java)
                    cameraLauncher.launch(intent)
                } else {
                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Start Camera Scan")
        }
    }
}
