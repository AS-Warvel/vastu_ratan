package com.example.project_vastuapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.project_vastuapp.datalayer.VastuLogic
import com.example.project_vastuapp.datalayer.VastuRepository
import com.example.project_vastuapp.ui.theme.VastuAppTheme.VastuAppTheme
import com.example.project_vastuapp.ui_layer.screens.*
import com.example.project_vastuapp.ui_layer.state.FurnitureViewModel
import com.example.project_vastuapp.ui_layer.state.RoomViewModel
import com.example.project_vastuapp.viewmodel.VastuAppViewModel
import com.example.project_vastuapp.viewmodel.VastuViewModelFactory
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            VastuAppTheme {
                val navController = rememberNavController()
                val roomInspectionViewModel: RoomViewModel = viewModel()
                val furnitureViewModel: FurnitureViewModel = viewModel()

                // Create VastuRepository with Context and Firestore
                val vastuRepository = VastuRepository(
                    context = applicationContext,
                    db = FirebaseFirestore.getInstance()
                )

                // Create VastuAppViewModel with proper dependencies
                val vastuAppViewModel: VastuAppViewModel = viewModel(
                    factory = VastuViewModelFactory(
                        vastuLogic = VastuLogic(vastuRepository),
                        vasturepository = vastuRepository
                    )
                )

                NavHost(navController = navController, startDestination = "splash") {
                    composable("home") { HomeScreen(navController) }
                    composable("VastuIntro") { VastuIntroScreen(navController) }
                    composable("VastuTips") { VastuTipsScreen(navController = navController) }
                    composable("Geeta") { LifeLessonsScreen(navController = navController) }
                    composable("Furniture") {
                        FurnitureInspectionScreen(
                            navController = navController,
                            furnitureViewModel = furnitureViewModel
                        )
                    }
                    composable("Results") {
                        NewResultsScreen(
                            navController = navController,
                            viewModel = roomInspectionViewModel
                        )
                    }
                    composable("Room") {
                        RoomInspectionScreen(
                            navController = navController,
                            viewModel = roomInspectionViewModel
                        )
                    }
                    composable("Compass") { VastuCompassScreen(navController = navController) }

                    // Pass the ViewModel to HomePage
                    composable("CameraPage") {
                        HomePage(navController= navController, vastuAppViewModel)
                    }
                    composable("ResultScreen") {
                        ResultScreen(
                            navController = navController,
                            myViewModel = vastuAppViewModel
                        )
                    }



                    composable(
                        route = "House/{value}",
                        arguments = listOf(navArgument("value") { type = NavType.FloatType })
                    ) { backStackEntry ->
                        val value = backStackEntry.arguments?.getFloat("value")?.toDouble() ?: 0.0
                        HouseLayouScreen(navController = navController, northAngle = value)
                    }

                    composable("splash") {
                        SplashScreen(
                            onTimeout = {
                                navController.navigate("home") {
                                    popUpTo("splash") { inclusive = true }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}