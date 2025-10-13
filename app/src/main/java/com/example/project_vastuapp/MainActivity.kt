package com.example.project_vastuapp
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.example.project_vastuapp.ui_layer.screens.HomePage
import com.example.project_vastuapp.ui_layer.screens.ResultScreen
import com.example.project_vastuapp.datalayer.VastuRepository
import com.example.project_vastuapp.datalayer.VastuLogic
import com.example.project_vastuapp.viewmodel.VastuAppViewModel
import com.example.project_vastuapp.viewmodel.VastuViewModelFactory
import com.example.project_vastuapp.datalayer.VastuCalculatorUseCase
import com.example.project_vastuapp.datalayer.VastuSensorManager
import com.example.project_vastuapp.datalayer.VastuAnalyzer
class MainActivity : ComponentActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var vastuRepository: VastuRepository
    private lateinit var vastuLogic: VastuLogic
    private lateinit var viewModel: VastuAppViewModel
    private lateinit var vastuAnalyzer: VastuAnalyzer         // NEW
    private lateinit var vastuSensorManager: VastuSensorManager // NEW
    private lateinit var vastuCalculatorUseCase: VastuCalculatorUseCase // NEW


    // MainActivity.kt - Inside onCreate

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Step 1 & 2: Initialize Firebase & Firestore (UNCHANGED)
        FirebaseApp.initializeApp(this)
        db = FirebaseFirestore.getInstance()

        // ==========================================================
        // STEP A: INITIALIZE NEW REAL-TIME COMPONENTS (Data/Domain)
        // ==========================================================

        // Instantiate the VastuAnalyzer (needs no arguments, but needs to exist)
        vastuAnalyzer = VastuAnalyzer()

        // Instantiate the VastuSensorManager (needs Context)
        vastuSensorManager = VastuSensorManager(this.applicationContext)

        // Instantiate the VastuCalculatorUseCase (Domain Logic - needs no arguments)
        vastuCalculatorUseCase = VastuCalculatorUseCase()

        // ==========================================================
        // STEP B: UPDATE DEPENDENCY INJECTION
        // ==========================================================

        // Step 3: Create Repository (Data Layer) - MUST PASS NEW DEPENDENCIES
        vastuRepository = VastuRepository(
            context = this.applicationContext, // Required for CameraX/Sensors
            db = db,
            vastuAnalyzer = vastuAnalyzer,
            vastuSensorManager = vastuSensorManager
        )

        // Step 4: Create Business Logic (Existing Manual Logic)
        // Assuming VastuLogic is still needed for manual checks (may need dependency update)
        // If VastuLogic only depends on VastuRepository for the manual Firebase check:
        vastuLogic = VastuLogic(vastuRepository)

        // Step 5: Create ViewModel Factory - MUST PASS NEW DEPENDENCIES
        // Assuming VastuLogic is for manual and VastuCalculatorUseCase is for real-time:
        val factory = VastuViewModelFactory(
            vastuLogic = vastuLogic, // For manual checks
            vastuRepository = vastuRepository, // For starting the camera
            vastuCalculatorUseCase = vastuCalculatorUseCase // For real-time processing
        )

        // Step 6 & 7: Get ViewModel instance and Set up UI (UNCHANGED)
        viewModel = ViewModelProvider(this, factory)[VastuAppViewModel::class.java]

        setContent {
            val currentPage by viewModel.currentPage.collectAsState()

            when (currentPage) {
                "Home" -> HomePage(myViewModel = viewModel)
                "Result Screen" -> ResultScreen(myViewModel = viewModel)
                else -> HomePage(myViewModel = viewModel)
            }
        }
    }
}