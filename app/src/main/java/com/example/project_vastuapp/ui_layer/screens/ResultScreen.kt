package com.example.project_vastuapp.ui_layer.screens
import FurnitureListItemCard
import GoToHomePageButton
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.project_vastuapp.ui.theme.background
import com.example.project_vastuapp.ui_layer.widgets.reusable_components.AppBar
import com.example.project_vastuapp.viewmodel.VastuAppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(myViewModel: VastuAppViewModel= viewModel()) {
    var expanded by remember { mutableStateOf(false) }
    Column(Modifier.background(background),horizontalAlignment = Alignment.CenterHorizontally){
        AppBar()

        val furnitureObjects by myViewModel.furnitureOutputList.collectAsState()
        LazyColumn(Modifier.weight(0.85f)) {
            items(furnitureObjects) { item ->
                FurnitureListItemCard(item)
            }
        }
        Box(Modifier.weight(0.15f)) {
            GoToHomePageButton(onClick = {myViewModel.navigateTo("Home")})
        }
    }
}