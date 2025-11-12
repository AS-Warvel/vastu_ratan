package com.example.project_vastuapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_vastuapp.datalayer.FurnitureAnalysisOutput
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.project_vastuapp.datalayer.FurnitureInput
import com.example.project_vastuapp.datalayer.VastuItem
import com.example.project_vastuapp.datalayer.VastuLogic
import com.example.project_vastuapp.datalayer.VastuRepository

class VastuAppViewModel(
    private val vastuLogic: VastuLogic,
    private val vasturepository: VastuRepository

) : ViewModel() {

    private val _currentPage = MutableStateFlow("Home")
    val currentPage: StateFlow<String> = _currentPage.asStateFlow()

    private val _furnitureInputObjects = MutableStateFlow<List<FurnitureInput>>(emptyList())
    val furnitureInputObjects: StateFlow<List<FurnitureInput>> = _furnitureInputObjects.asStateFlow()

    private val _furnitureOutputList = MutableStateFlow<List< FurnitureAnalysisOutput>>(emptyList())
    val furnitureOutputList: StateFlow<List<FurnitureAnalysisOutput>> = _furnitureOutputList.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _vastuItems = MutableStateFlow<List<VastuItem>>(emptyList())
    val vastuItems: StateFlow<List<VastuItem>> = _vastuItems.asStateFlow()

    private val _isFetching = MutableStateFlow(false)
    val isFetching: StateFlow<Boolean> = _isFetching.asStateFlow()
    init {
        Log.d("VastuVM", "ViewModel initialized")
    }

    fun navigateTo(page: String) {
        Log.d("VastuVM", "Navigating to: $page")
        _currentPage.value = page
    }

    fun fetchVastuItems() {
        _isFetching.value = true
        vasturepository.getAllVastuItems(
            onSuccess = { list ->
                _vastuItems.value = list
                _isFetching.value = false
            },
            onError = { e ->
                e.printStackTrace()
                _isFetching.value = false
            }
        )
    }

    fun setFurnitureInputObjects(listObjects: List<FurnitureInput>) {
        Log.d("VastuVM", "Setting ${listObjects.size} furniture inputs")
        listObjects.forEach {
            Log.d("VastuVM", "  - ${it.furnitureType}: ${it.direction}")
        }
        _furnitureInputObjects.value = listObjects
    }

    fun evaluateFurnitureList() {
        val inputList = _furnitureInputObjects.value
        executeAnalysisFlow(inputList)
    }

    private fun executeAnalysisFlow(inputs: List<FurnitureInput>) {
        vastuLogic.evaluateFurnitureInputList(
            furnitureInputList = inputs,
            callback = object : VastuLogic.VastuOutputCallback {
                override fun onAnalysisComplete(outputList: List<FurnitureAnalysisOutput>) {
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
