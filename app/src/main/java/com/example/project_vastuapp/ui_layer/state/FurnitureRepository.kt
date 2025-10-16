package com.example.project_vastuapp.ui_layer.state

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import com.example.project_vastuapp.data_layer.FurnitureDataSource

//import com.example.project_vastuapp.data_layer.FurnitureDataSource

data class VastuObject(
    val room: String,
    val furnitureType: String,
    val icon: ImageVector,
    val correctDirection: String,
    val whyThisDirection: String,
    val directionsToAvoid: String? = null
)

data class PlacementVastuObject(
    val room: String,
    val `object`: String, // 'object' is a keyword in Kotlin, so use backticks
    val recommendedDirections: List<String>,
)

object FurnitureInspectionDataSource {

    // Helper function to map object names to Material Icons
    private fun getIconForObject(objectName: String): ImageVector {
        return when {
            objectName.contains("Sofa", ignoreCase = true) -> Icons.Outlined.Chair
            objectName.contains("Bed", ignoreCase = true) -> Icons.Outlined.Bed
            objectName.contains("Plants", ignoreCase = true) -> Icons.Outlined.Yard
            objectName.contains("TV", ignoreCase = true) -> Icons.Outlined.Tv
            objectName.contains("Table", ignoreCase = true) -> Icons.Outlined.TableRestaurant
            objectName.contains("Door", ignoreCase = true) -> Icons.Outlined.DoorFront
            objectName.contains("Clock", ignoreCase = true) -> Icons.Outlined.Schedule
            objectName.contains("Cabinet", ignoreCase = true) || objectName.contains("Wardrobe", ignoreCase = true) -> Icons.Outlined.Inventory2
            objectName.contains("Mirror", ignoreCase = true) -> Icons.Outlined.FilterFrames
            objectName.contains("Aquarium", ignoreCase = true) -> Icons.Outlined.Water
            objectName.contains("Safe", ignoreCase = true) -> Icons.Outlined.Savings
            objectName.contains("Stove", ignoreCase = true) -> Icons.Outlined.OutdoorGrill
            objectName.contains("Sink", ignoreCase = true) -> Icons.Outlined.Countertops
            objectName.contains("Refrigerator", ignoreCase = true) -> Icons.Outlined.Kitchen
            objectName.contains("Desk", ignoreCase = true) -> Icons.Outlined.Desk
            objectName.contains("Chair", ignoreCase = true) -> Icons.Outlined.Chair
            objectName.contains("Bookshelf", ignoreCase = true) -> Icons.Outlined.Book
            objectName.contains("Computer", ignoreCase = true) -> Icons.Outlined.Laptop
            objectName.contains("Lamp", ignoreCase = true) -> Icons.Outlined.Lightbulb
            objectName.contains("Dustbin", ignoreCase = true) -> Icons.Outlined.Delete
            objectName.contains("Document", ignoreCase = true) -> Icons.Outlined.Folder
            objectName.contains("Meditation", ignoreCase = true) -> Icons.Outlined.CenterFocusStrong
            objectName.contains("Board", ignoreCase = true) -> Icons.Outlined.Draw
            objectName.contains("Water", ignoreCase = true) -> Icons.Outlined.WaterDrop
            objectName.contains("Fan", ignoreCase = true) -> Icons.Outlined.WindPower
            objectName.contains("Portraits", ignoreCase = true) -> Icons.Outlined.Portrait


            else -> Icons.Outlined.Home
        }
    }

    suspend fun getFurnitureObjects() : List<VastuObject> {
        val listFurnitureData = FurnitureDataSource.getFurnitureData()

        val furnitureList = mutableListOf<VastuObject>()
        for (item in listFurnitureData) {
            furnitureList.add(
                VastuObject(
                    item.room,
                    item.furnitureType,
                    getIconForObject(item.furnitureType),
                    item.direction,
                    item.benefits,
                    item.avoid
            )
            )
        }

        return furnitureList
    }

    suspend fun getRoomFurnitureObjects() : List<RoomFurnitureItem> {
        val listFurnitureData = FurnitureDataSource.getFurnitureData()

        val furnitureList = mutableListOf<RoomFurnitureItem>()
        for (item in listFurnitureData) {
            val directions = item.direction.split(Regex(" or | / |,"))
                .map { it.trim() }
            furnitureList.add(
                RoomFurnitureItem(
                    furnitureType = item.furnitureType,
                    correctDirections = directions,
                    whyThisDirection = item.benefits,
                    directionsToAvoid = item.avoid,
                    room = item.room,
                    icon = getIconForObject(item.furnitureType),
                    )
            )
        }
        return furnitureList
    }

    suspend fun getObjectsForPlacement() : List<PlacementVastuObject> {
        val listFurnitureData = FurnitureDataSource.getFurnitureData()

        val furnitureList = mutableListOf<PlacementVastuObject>()
        for (item in listFurnitureData) {
            val directions = item.direction.split(Regex(" or | / |,"))
                .map { it.trim() }
            furnitureList.add(
                PlacementVastuObject(
                    room = item.room,
                    `object` = item.furnitureType,
                    recommendedDirections = directions
                )
            )
        }
        return furnitureList
    }
}
