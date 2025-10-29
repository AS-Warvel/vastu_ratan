package com.example.project_vastuapp.ui_layer.state

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.util.UUID


// --- 2. DATA CLASSES AND STATE ---

data class RoomFurnitureItem(
    val id: String = UUID.randomUUID().toString(),
    val room: String,
    val furnitureType: String,
    val correctDirections: List<String>,
    val icon: ImageVector,
    val whyThisDirection: String,
    val directionsToAvoid: String?,
    var currentSelectedDirection: String = "Not Present"
)

data class RoomUiState(
    val allItems: List<RoomFurnitureItem> = emptyList(),
    val rooms: List<String> = emptyList(),
    val selectedRoom: String = "",
    val itemsForSelectedRoom: List<RoomFurnitureItem> = emptyList()
)


// --- 3. VIEWMODEL ---

class RoomViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(RoomUiState())
    val uiState: StateFlow<RoomUiState> = _uiState.asStateFlow()

    private val directions = listOf(
        "Not Present", "North", "Northeast", "East", "Southeast", "South", "Southwest", "West", "Northwest"
    )

    init {
        loadFurnitureData()
    }

    private fun loadFurnitureData() {
            viewModelScope.launch {
                val allItems = FurnitureInspectionDataSource.getRoomFurnitureObjects()
                val rooms = allItems.map { it.room }.distinct()
                val initialRoom = rooms.firstOrNull() ?: ""
                _uiState.value = RoomUiState(
                    allItems = allItems,
                    rooms = rooms,
                    selectedRoom = initialRoom,
                    itemsForSelectedRoom = allItems.filter { it.room == initialRoom }
                )
            }
//        val allItems = FurnitureInspectionDataSource.getRoomFurnitureObjects()

    }

    fun getDirectionOptions(): List<String> = directions

    fun selectRoom(room: String) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedRoom = room,
                itemsForSelectedRoom = currentState.allItems.filter { it.room == room }
            )
        }
    }

    fun updateDirection(itemId: String, newDirection: String) {
        _uiState.update { currentState ->
            val updatedAllItems = currentState.allItems.map { item ->
                if (item.id == itemId) {
                    item.copy(currentSelectedDirection = newDirection)
                } else {
                    item
                }
            }
            currentState.copy(
                allItems = updatedAllItems,
                itemsForSelectedRoom = updatedAllItems.filter { it.room == currentState.selectedRoom }
            )
        }
    }

    fun isDirectionCorrect(item: RoomFurnitureItem): Boolean {
        if (item.currentSelectedDirection == "Not Present") return true // Or handle as neutral

        // A simple check if the selected direction abbreviation is part of the recommended text
        val recommendedText = item.correctDirections.joinToString(" ").lowercase()
        val selectedDir = item.currentSelectedDirection.lowercase()

        // Handle cardinal directions (N, S, E, W)
        val cardinalMap = mapOf(
            "n" to "north", "s" to "south", "e" to "east", "w" to "west"
        )

        return recommendedText.contains(selectedDir)
    }
}

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


// --- 7. UTILITIES AND DATA ---

/**
 * Parses the raw JSON string into a list of FurnitureItem objects.
 */
fun parseVastuData(jsonString: String): List<RoomFurnitureItem> {
    val furnitureList = mutableListOf<RoomFurnitureItem>()
    val jsonArray = JSONArray(jsonString)
    for (i in 0 until jsonArray.length()) {
        val jsonObject = jsonArray.getJSONObject(i)
        val recommendedDirections = jsonObject.getString("recommended_direction")
            // Simple parsing logic
            .split(Regex(" or | / |,"))
            .map { it.trim() }

        furnitureList.add(
            RoomFurnitureItem(
                room = jsonObject.getString("room"),
                furnitureType = jsonObject.getString("object"),
                correctDirections = recommendedDirections,
                whyThisDirection = jsonObject.getString("benefit"),
                directionsToAvoid = jsonObject.optString("avoid", null),
                icon = Icons.Default.Star
            )
        )
    }
    return furnitureList
}

/**
 * Returns a drawable resource ID for a given furniture type.
 * Note: You must add these drawable resources to your project's `res/drawable` folder.
 * I'll use placeholder vector icons for this example.
 */
@Composable
fun getIconForFurniture(type: String): Int {
    val context = LocalContext.current
    val resourceName = "ic_" + type.lowercase().replace(" ", "_").replace("/", "_")
    val resourceId = context.resources.getIdentifier(resourceName, "drawable", context.packageName)

    // Return a default icon if a specific one isn't found
    return if (resourceId != 0) resourceId else R.drawable.ic_default_furniture
}

// Dummy drawable resources. In a real project, create these files in `res/drawable`.
// For this example, we'll just use a placeholder to make the code compile.
// To make this work, create a file named `ic_default_furniture.xml` in `res/drawable`.
// For example:
// <vector xmlns:android="http://schemas.android.com/apk/res/android"
//     android:width="24dp"
//     android:height="24dp"
//     android:viewportWidth="24"
//     android:viewportHeight="24">
//   <path
//       android:fillColor="#000000"
//       android:pathData="M20,2H4C2.9,2 2,2.9 2,4v16h2V4h16V2zM18,6H8C6.9,6 6,6.9 6,8v12c0,1.1 0.9,2 2,2h10c1.1,0 2,-0.9 2,-2V8C20,6.9 19.1,6 18,6zM18,20H8V8h10V20z"/>
// </vector>
object R {
    object drawable {
        // This is a placeholder. You'll need actual drawable resources.
        // For demonstration, we'll just use a default that won't resolve.
        // Add `ic_default_furniture.xml` to your `res/drawable` folder.
        val ic_default_furniture: Int = android.R.drawable.ic_menu_gallery
    }
}


val vastuJsonData = """
[
  {
    "room": "Living Room",
    "object": "Sofa Set",
    "recommended_direction": "South or West wall",
    "benefit": "Promotes stability and grounding in social interactions.",
    "avoid": "North-East – blocks spiritual and energy flow."
  },
  {
    "room": "Living Room",
    "object": "TV / Entertainment Unit",
    "recommended_direction": "South-East wall",
    "benefit": "Keeps electronic energy aligned with Fire element.",
    "avoid": "North wall – causes restlessness."
  },
  {
    "room": "Living Room",
    "object": "Main Entrance Door",
    "recommended_direction": "North or East",
    "benefit": "Attracts positive energy and prosperity.",
    "avoid": "South-West – brings obstacles and conflict."
  },
  {
    "room": "Living Room",
    "object": "Center Table",
    "recommended_direction": "Center (Brahmasthan), keep light and low",
    "benefit": "Allows free energy flow.",
    "avoid": "Center with heavy furniture – blocks energy flow."
  },
  {
    "room": "Living Room",
    "object": "Wall Clock",
    "recommended_direction": "North or East wall",
    "benefit": "Symbol of progress and positive movement.",
    "avoid": "South wall – associated with stagnation."
  },
  {
    "room": "Living Room",
    "object": "Showcase / Cabinet",
    "recommended_direction": "South or West wall",
    "benefit": "Balances heavy elements, giving stability.",
    "avoid": "North-East – adds heaviness to divine corner."
  },
  {
    "room": "Living Room",
    "object": "Mirror / Decor Mirror",
    "recommended_direction": "North or East wall",
    "benefit": "Reflects positive energy and abundance.",
    "avoid": "South or West – reflects negative energy."
  },
  {
    "room": "Living Room",
    "object": "Aquarium / Water Feature",
    "recommended_direction": "North or East",
    "benefit": "Brings calmness, wealth, and harmony.",
    "avoid": "South – Fire element conflict."
  },
  {
    "room": "Living Room",
    "object": "Indoor Plants",
    "recommended_direction": "North-East or East",
    "benefit": "Purifies energy and enhances freshness.",
    "avoid": "South-West – causes energy imbalance."
  },
  {
    "room": "Master Bedroom",
    "object": "Bed",
    "recommended_direction": "Head towards South or East",
    "benefit": "Promotes sound sleep, health, and longevity.",
    "avoid": "North – may cause disturbed sleep."
  },
  {
    "room": "Master Bedroom",
    "object": "Wardrobe / Almirah",
    "recommended_direction": "South-West",
    "benefit": "Symbol of financial stability and strength.",
    "avoid": "North-East – adds heaviness to sacred corner."
  },
  {
    "room": "Master Bedroom",
    "object": "Dressing Table / Mirror",
    "recommended_direction": "North or East wall (avoid facing bed)",
    "benefit": "Enhances positivity and freshness.",
    "avoid": "Facing bed – reflects energy back during sleep."
  },
  {
    "room": "Master Bedroom",
    "object": "TV / Electronics",
    "recommended_direction": "South-East wall",
    "benefit": "Keeps Fire element balanced.",
    "avoid": "North-East – disrupts peace and calmness."
  },
  {
    "room": "Master Bedroom",
    "object": "Safe / Locker",
    "recommended_direction": "South wall facing North",
    "benefit": "Attracts wealth and prosperity.",
    "avoid": "East or North-East – causes financial instability."
  },
  {
    "room": "Master Bedroom",
    "object": "Clock / Artwork",
    "recommended_direction": "East wall",
    "benefit": "Maintains positive flow of time energy.",
    "avoid": "South – symbolizes delay and struggle."
  },
  {
    "room": "Kitchen",
    "object": "Cooking Stove / Gas Range",
    "recommended_direction": "South-East corner",
    "benefit": "Aligns with Fire element and promotes health.",
    "avoid": "North-East – clashes with Water element."
  },
  {
    "room": "Kitchen",
    "object": "Sink / Wash Area",
    "recommended_direction": "North-East",
    "benefit": "Maintains purity and balance of Water element.",
    "avoid": "South-East – Fire-Water conflict."
  },
  {
    "room": "Office / Study Room",
    "object": "Study / Work Desk",
    "recommended_direction": "East or North facing",
    "benefit": "Improves focus, clarity, and concentration.",
    "avoid": "South – drains energy and motivation."
  }
]
"""