package com.example.project_vastuapp.data_layer

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object FurnitureDataSource {
    var furnitureObjects: List<FurnitureDataModel> = emptyList()

    suspend fun fetchAllFurnitureData(): List<FurnitureDataModel> {
        val firestore = FirebaseFirestore.getInstance()
        val collectionName = "FurnitureObjectsData"

        return try {
            val snapshot = firestore.collection(collectionName).get().await()
            snapshot.documents.mapNotNull { doc ->
                val furnitureType = doc.getString("object") ?: ""
                val direction = doc.getString("recommended_direction") ?: ""
                val benefits = doc.getString("benefit") ?: ""
                val avoid = doc.getString("avoid") ?: ""
                val room = doc.getString("room") ?: ""
                FurnitureDataModel(
                    furnitureType = furnitureType,
                    direction = direction,
                    benefits = benefits,
                    avoid = avoid,
                    room = room
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getFurnitureData(): List<FurnitureDataModel> {
        if (furnitureObjects.isNotEmpty()) {

            println(furnitureObjects)
            return furnitureObjects
        }

        val listFurnitureData = fetchAllFurnitureData()
            furnitureObjects = listFurnitureData
            print(listFurnitureData)
            return listFurnitureData
    }
}