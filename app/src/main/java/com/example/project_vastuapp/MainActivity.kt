package com.example.project_vastuapp

import FurnitureInput
//import ResultScreen
//import VastuAppViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.viewmodel.compose.viewModel
//import com.example.project_vastuapp.ui_layer.screens.HomePage
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project_vastuapp.ui.theme.ColorDarkOlive
import com.example.project_vastuapp.ui.theme.ColorWhite
import com.example.project_vastuapp.ui_layer.screens.HomeScreen
import com.example.project_vastuapp.ui.theme.VastuAppTheme.VastuAppTheme
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.project_vastuapp.ui_layer.screens.FurnitureInspectionScreen
import com.example.project_vastuapp.ui_layer.screens.HouseLayouScreen
import com.example.project_vastuapp.ui_layer.screens.LifeLessonsScreen
import com.example.project_vastuapp.ui_layer.screens.NewResultsScreen
import com.example.project_vastuapp.ui_layer.screens.RoomInspectionScreen
import com.example.project_vastuapp.ui_layer.screens.VastuIntroScreen
import com.example.project_vastuapp.ui_layer.screens.VastuTipsScreen
import com.example.project_vastuapp.ui_layer.screens.VastuCompassScreen
import com.example.project_vastuapp.ui_layer.state.RoomViewModel

// Main entry point for the App
@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VastuAppTheme {
                val navController = rememberNavController()
                val RoomInspectionViewModel: RoomViewModel = viewModel()
                NavHost(navController = navController, startDestination = "home") {
                    composable("home") { HomeScreen(navController) }
                    composable("VastuIntro") { VastuIntroScreen(navController) }
                    composable("VastuTips") { VastuTipsScreen(navController = navController) }
                    composable("Geeta") { LifeLessonsScreen(navController = navController) }
                    composable("Furniture") { FurnitureInspectionScreen(navController = navController) }
                    composable("Results") { NewResultsScreen(navController = navController, viewModel = RoomInspectionViewModel) }
                    composable("Room") { RoomInspectionScreen(navController = navController, viewModel = RoomInspectionViewModel) }
                    composable("Compass") { VastuCompassScreen(navController = navController) }
                    composable(
                        route = "House/{value}",
                        arguments = listOf(navArgument("value") { type = NavType.FloatType })
                    ) {
                        backStackEntry ->
                        val value = backStackEntry.arguments?.getFloat("value")?.toDouble() ?: 0.0
                        HouseLayouScreen(navController = navController, northAngle = value) }
//                    composable("menu") { MenuScreen(navController) }
                }
            }
        }
    }
}

// Custom Theme Wrapper


//class MainActivity : ComponentActivity() {
//    @ExperimentalMaterial3Api
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContent {
//            val vm: VastuAppViewModel = viewModel()
//            var inputForResultScreen = remember { mutableStateListOf<FurnitureInput>() }
//
//            val currentPage = vm.currentPage.collectAsState().value
//
//
//            if(currentPage == "Home") {
//                HomePage()
//            }
//            else if (currentPage == "Result Screen") {
//                ResultScreen()
//            }
//        }
//    }
//}