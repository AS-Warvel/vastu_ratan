package com.example.project_vastuapp

import FurnitureInput
import ResultScreen
import VastuAppViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.project_vastuapp.ui_layer.screens.HomePage
import androidx.compose.runtime.collectAsState

class MainActivity : ComponentActivity() {
    @ExperimentalMaterial3Api
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val vm: VastuAppViewModel = viewModel()
            var inputForResultScreen = remember { mutableStateListOf<FurnitureInput>() }

            val currentPage = vm.currentPage.collectAsState().value


            if(currentPage == "Home") {
                HomePage()
            }
            else if (currentPage == "Result Screen") {
                ResultScreen()
            }
        }
    }
}