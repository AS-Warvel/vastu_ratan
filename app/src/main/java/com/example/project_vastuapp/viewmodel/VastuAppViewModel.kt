package com.example.project_vastuapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.project_vastuapp.datalayer.FurnitureInput
import com.example.project_vastuapp.datalayer.FurnitureOutput
import com.example.project_vastuapp.datalayer.VastuRepository // NEW
import com.example.project_vastuapp.datalayer.VastuCalculatorUseCase // NEW
import com.example.project_vastuapp.datalayer.VastuAnalysisResult // NEW
import com.example.project_vastuapp.datalayer.VastuLogic // Existing dependency

// We will use a dedicated State for the Camera Scan since it's a real-time process
sealed class VastuScanState {
    data object Idle : VastuScanState()
    data class Error(val message: String) : VastuScanState()
    data class Scanning(val results: List<VastuAnalysisResult>) : VastuScanState() // Real-time results
}
sealed class VastuModeState {
    data object SelectMode : VastuModeState() // Initial state: show selection buttons
    data object ManualInput : VastuModeState() // User clicked 'Manual Check'
    data object CameraScan : VastuModeState()  // User clicked 'Camera Scan'
}

class VastuAppViewModel(
    private val vastuLogic: VastuLogic, // Existing dependency for manual checks
    private val vastuRepository: VastuRepository, // NEW dependency for camera data
    private val vastuCalculatorUseCase: VastuCalculatorUseCase // NEW dependency for real-time logic
) : ViewModel() {


    private val _currentMode = MutableStateFlow<VastuModeState>(VastuModeState.SelectMode)
    val currentMode: StateFlow<VastuModeState> = _currentMode.asStateFlow()

    // New function to handle button clicks
    fun setMode(mode: VastuModeState) {
        _currentMode.value = mode
    }

    // Add a function to reset the mode, maybe for a back button
    fun resetMode() {
        // Also stop camera processing if it was running (implement this logic in Repository later)
        // vastuRepository.unbindCamera()
        _currentMode.value = VastuModeState.SelectMode
    }
    // --- UI/NAVIGATION STATE (Existing) ---
    private val _currentPage = MutableStateFlow("Home")
    val currentPage: StateFlow<String> = _currentPage.asStateFlow()
    // ... (Existing FurnitureInputObjects, FurnitureOutputList, Loading, Error States) ...

    private val _furnitureInputObjects = MutableStateFlow<List<FurnitureInput>>(emptyList())
    val furnitureInputObjects: StateFlow<List<FurnitureInput>> = _furnitureInputObjects.asStateFlow()

    // --- CRITICAL RESULT STATE ---
    private val _furnitureOutputList = MutableStateFlow<List<FurnitureOutput>>(emptyList())
    val furnitureOutputList: StateFlow<List<FurnitureOutput>> = _furnitureOutputList.asStateFlow()

    // --- NEW: REAL-TIME CAMERA SCAN STATE ---
    private val _vastuScanState = MutableStateFlow<VastuScanState>(VastuScanState.Idle)
    val vastuScanState: StateFlow<VastuScanState> = _vastuScanState.asStateFlow()

    // --- LOADING/ERROR STATE ---
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        Log.d("VastuVM", "ViewModel initialized")
    }

    // --- NEW: CAMERA SCAN HANDLERS ---
    fun startVastuScan(lifecycleOwner: LifecycleOwner, previewView: PreviewView) {
        _vastuScanState.value = VastuScanState.Idle

        // 1. Tell the repository to bind CameraX components
        vastuRepository.bindCameraToLifecycle(lifecycleOwner, previewView)

        // 2. Start collecting the combined raw data stream
        collectRealtimeScanData()
    }

    private fun collectRealtimeScanData() {
        viewModelScope.launch {
            vastuRepository.getRealtimeVastuScanResults()
                .collect { rawData ->

                    if (rawData.rawObjects.isEmpty()) {
                        _vastuScanState.value = VastuScanState.Idle
                        return@collect
                    }

                    // 3. Apply Domain Logic to convert raw data to final analysis results
                    val processedResults = vastuCalculatorUseCase.calculateVastuScore(rawData)

                    // 4. Update the real-time UI State (Homepage will observe this)
                    _vastuScanState.value = VastuScanState.Scanning(processedResults)
                }
        }
    }

    // --- NAVIGATION AND INPUT HANDLERS (Existing - UNCHANGED) ---
    fun navigateTo(page: String) {
        Log.d("VastuVM", "Navigating to: $page")
        _currentPage.value = page
    }

    fun setFurnitureInputObjects(listObjects: List<FurnitureInput>) {
        Log.d("VastuVM", "Setting ${listObjects.size} furniture inputs")
        listObjects.forEach {
            Log.d("VastuVM", "  - ${it.furnitureType}: ${it.direction}")
        }
        _furnitureInputObjects.value = listObjects
    }

    // --- ANALYSIS EXECUTION (Existing - UNCHANGED) ---
    fun evaluateFurnitureList() {
        val inputList = _furnitureInputObjects.value
        // ... (Existing logic for executing analysis flow) ...

        executeAnalysisFlow(inputList)
    }

    private fun executeAnalysisFlow(inputs: List<FurnitureInput>) {
        // ... (Existing logic using vastuLogic.evaluateFurnitureInputList) ...
        vastuLogic.evaluateFurnitureInputList(
            furnitureInputList = inputs,
            callback = object : VastuLogic.VastuOutputCallback {
                // ... (Existing onAnalysisComplete and onError logic) ...
                override fun onAnalysisComplete(outputList: List<FurnitureOutput>) {
                    Log.d("VastuVM", "✅ Analysis complete: ${outputList.size} items processed")
                    _furnitureOutputList.value = outputList
                    _isLoading.value = false
                    navigateTo("Result Screen")
                }

                override fun onError(exception: Exception) {
                    Log.e("VastuVM", "❌ Error during analysis: ${exception.message}", exception)
                    _errorMessage.value = exception.message ?: "Unknown error occurred"
                    _isLoading.value = false
                }
            }
        )
    }

    fun clearResults() {
        Log.d("VastuVM", "Clearing all results")
        _furnitureOutputList.value = emptyList()
        _furnitureInputObjects.value = emptyList()
        _errorMessage.value = null
    }

    fun clearError() {
        _errorMessage.value = null
    }
}