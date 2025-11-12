package com.example.project_vastuapp.ui_layer.state

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_vastuapp.data_layer.FurnitureDataSource
import com.example.project_vastuapp.data_layer.VastuObject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID
import android.util.Log

class FurnitureViewModel : ViewModel() {

    private var _allObjects: List<VastuObject> = emptyList()
    var rooms: List<String> = emptyList()

    private val _selectedRoom = MutableStateFlow("dummy")
    var selectedRoom: StateFlow<String> = _selectedRoom.asStateFlow()

    // Holds furniture list for the selected room
    var furnitureForSelectedRoom: StateFlow<List<VastuObject>> =
        selectedRoom.combine(MutableStateFlow(_allObjects)) { room, objects ->
            objects.filter { it.room == room }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedFurniture = MutableStateFlow<VastuObject?>(null)
    val selectedFurniture: StateFlow<VastuObject?> = _selectedFurniture.asStateFlow()

    init {
        load()
    }

    private fun load() {
        viewModelScope.launch {
            try {
                // 🔹 Load all furniture objects safely
                _allObjects = FurnitureInspectionDataSource.getFurnitureObjects()
                rooms = _allObjects.map { it.room }.distinct()

                Log.d("FurnitureViewModel", "Loaded ${_allObjects.size} objects.")
                Log.d("FurnitureViewModel", "Rooms found: $rooms")

                if (rooms.isEmpty()) {
                    // Prevent crash — handle empty data gracefully
                    Log.w("FurnitureViewModel", "No rooms found in data source.")
                    return@launch
                }

                // 🔹 Only select the first room if we have at least one
                selectRoom(rooms.first())

                // 🔹 Update flow for room-wise furniture
                furnitureForSelectedRoom = selectedRoom.combine(MutableStateFlow(_allObjects)) { room, objects ->
                    objects.filter { it.room == room }
                }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

            } catch (e: Exception) {
                Log.e("FurnitureViewModel", "Error loading furniture data: ${e.message}", e)
            }
        }
    }

    fun selectRoom(room: String) {
        _selectedRoom.update { room }
        _selectedFurniture.update {
            _allObjects.firstOrNull { it.room == room } // ✅ null-safe
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
    private val dataSource: FurnitureDataSource = FurnitureDataSource
) : ViewModel() {

    private val _dataString = MutableStateFlow("Loading...")
    var dataString: StateFlow<String> = _dataString.asStateFlow()

    fun getFurniturePlacementReasonInfo(
        room: String,
        furnitureType: String,
        isDirectionCorrect: Boolean,
        default: String,
    ) {
        var colxnName = ""
        var docName = ""

        if (furnitureType.contains("Refrigerator", ignoreCase = true)) {
            colxnName = "kitchen_rules"
            docName = "refrigerator"
        } else {
            _dataString.value = "not found"
            return
        }

        viewModelScope.launch {
            try {
                _dataString.value = FurnitureDataSource.retrieveDetailsData(
                    collectionName = colxnName,
                    documentIdName = docName,
                    isFetchFavourableReason = isDirectionCorrect,
                )
            } catch (e: Exception) {
                _dataString.value = "Error: ${e.message}"
                Log.e("FurnitureDetailsViewModel", "Data fetch error: ${e.message}", e)
            }
        }
    }
}
