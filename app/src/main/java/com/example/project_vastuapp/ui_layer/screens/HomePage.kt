package com.example.project_vastuapp.ui_layer.screens

import AddFurnitureObjectDialogBox
import FurnitureInput
import VastuAppViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import com.example.project_vastuapp.ui.theme.background
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.project_vastuapp.ui_layer.widgets.CheckVastuButton
import com.example.project_vastuapp.ui_layer.widgets.VastuLogo
import com.example.project_vastuapp.ui_layer.widgets.reusable_components.AppBar

@ExperimentalMaterial3Api
@Composable
fun HomePage(
    myViewModel: VastuAppViewModel = viewModel(),
) {
    var addFurnitureObjects by remember { mutableStateOf(false) }
    var setFurnitureObjects: (List<FurnitureInput>) -> Unit = { furnitureInputObjects ->
        addFurnitureObjects = false
        myViewModel.setFurnitureInputObjects(furnitureInputObjects)
        myViewModel.navigateTo("Result Screen")
    }
    var cancelAddFurnitureDialogBox = {
        addFurnitureObjects = false
    }

    Column {
        AppBar(showActions = true)
        Column (Modifier.fillMaxSize().background(background),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(150.dp))
            Box(
                modifier = Modifier.padding(16.dp).scale(1.35f),
            ){
                VastuLogo()
            }
            Spacer(Modifier.height(280.dp))
            CheckVastuButton(enableAddFurnitureScreen = {addFurnitureObjects = true})

            if(addFurnitureObjects) {
                AddFurnitureObjectDialogBox(sendFurnitureObjects = setFurnitureObjects, cancelOperation = cancelAddFurnitureDialogBox)
            }
        }
    }
}
