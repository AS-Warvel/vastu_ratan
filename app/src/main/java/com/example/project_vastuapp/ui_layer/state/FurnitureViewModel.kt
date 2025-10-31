package com.example.project_vastuapp.ui_layer.state

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_vastuapp.data_layer.FurnitureDataSource
import com.example.project_vastuapp.data_layer.VastuObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class FurnitureViewModel : ViewModel() {

    private var _allObjects: List<VastuObject> = emptyList()
    var rooms: List<String> = emptyList<String>()
//    val rooms: List<String> = allObjects.value.map { it.room }.distinct()


    private val _selectedRoom = MutableStateFlow<String>("dummy")
    var selectedRoom: StateFlow<String> = _selectedRoom.asStateFlow()

    init {
        load()
    }
    private fun load() {
        viewModelScope.launch {
            _allObjects = FurnitureInspectionDataSource.getFurnitureObjects()
            rooms = _allObjects.map { it.room }.distinct()
//            _selectedRoom.value = rooms.value.first()
            selectRoom(rooms.first())
            furnitureForSelectedRoom = selectedRoom.combine(MutableStateFlow(_allObjects)) { room, objects ->
                objects.filter { it.room == room }
            }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
        }
    }

    var furnitureForSelectedRoom: StateFlow<List<VastuObject>> =
        selectedRoom.combine(MutableStateFlow(_allObjects)) { room, objects ->
            objects.filter { it.room == room }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedFurniture = MutableStateFlow<VastuObject?>(null)
    val selectedFurniture: StateFlow<VastuObject?> = _selectedFurniture.asStateFlow()



    fun selectRoom(room: String) {
        _selectedRoom.update { room }
        // When room changes, select the first furniture item from that room's list
        _selectedFurniture.update {
            _allObjects.firstOrNull { it.room == room }
        }
    }

    fun selectFurniture(furniture: VastuObject) {
        _selectedFurniture.update { furniture }
    }
}

/**
 * ViewModel to manage the UI state and interact with the DataSource.
 */
class FurnitureDetailsViewModel(
    // In a real app, this is usually injected using Hilt or Koin
    private val dataSource: FurnitureDataSource = FurnitureDataSource
) : ViewModel() {

    // Private MutableStateFlow to hold the string state internally
    private val _dataString = MutableStateFlow<String>("Loading...")

    // Public, read-only StateFlow exposed to the UI
    var dataString: StateFlow<String> = _dataString.asStateFlow()

    /**
     * Function called by the UI to fetch data.
     * It updates the dataString state flow.
     *
     * @param room The room selected in the UI.
     * @param furnitureType The type of furniture selected.
     * @param isDirectionCorrect A boolean flag from the UI.
     */
    fun getFurniturePlacementReasonInfo(
        room: String,
        furnitureType: String,
        isDirectionCorrect: Boolean,
        default: String,
    ) {
        var colxnName: String = ""
        var docName: String = ""
        // Use viewModelScope to launch a coroutine safely

        // Logic Here...
        if(furnitureType.contains("Refrigerator")) {
            colxnName = "kitchen_rules"
            docName = "refrigerator"
        }
        else {
            _dataString.value = "not found"
            return
        }
//        colxnName = "kitchen_rules"
//        docName = "stove"

        viewModelScope.launch {



            _dataString.value = FurnitureDataSource.retrieveDetailsData(
                collectionName = colxnName,
                documentIdName = docName,
                isFetchFavourableReason = isDirectionCorrect,
            )

            // 1. Set the state to "loading..." (as requested)
//            _dataString.value = "loading..."

            // 2. --- YOUR LOGIC GOES HERE ---
            // You can use the input parameters (room, furnitureType, etc.)
            // to decide what parameters to pass to the dataSource.

            // Example (you can replace this):
            // try {
            //     val collection = if (room == "Bedroom") "beds" else "chairs"
            //
            //     val result = dataSource.retrieveData(
            //         collectionName = collection,
            //         documentIdName = furnitureType,
            //         isFetchFavourableReason = isDirectionCorrect
            //     )
            //
            //     // 3. Update the state with the result
            //     _dataString.value = result
            //
            // } catch (e: Exception) {
            //     // 4. Handle any errors
            //     _dataString.value = "Error: ${e.message}"
            // }
            //
            // --- END OF YOUR LOGIC ---
        }
    }
}
//
//// /ui/MyFurnitureScreen.kt
//
//import androidx.compose.foundation.layout.Column
//import androidx.compose.material3.Button
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.lifecycle.viewmodel.compose.viewModel
//
//@Composable
//fun MyFurnitureScreen(
//    // Use viewModel() to get an instance of the FurnitureViewModel
//    viewModel: FurnitureViewModel = viewModel()
//) {
//    // Observe the dataString StateFlow.
//    // Compose will automatically recompose when this value changes.
//    val dataToShow by viewModel.dataString.collectAsState()
//
//    Column {
//        // This Text will show "loading..." and then the result
//        Text(text = dataToShow)
//
//        Button(onClick = {
//            // Call the ViewModel function
//            viewModel.getFurniturePlacementInfo(
//                room = "Living Room",
//                furnitureType = "Sofa",
//                isDirectionCorrect = true
//            )
//        }) {
//            Text("Fetch Data")
//        }
//    }
//}