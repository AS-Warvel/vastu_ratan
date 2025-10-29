package com.example.project_vastuapp.data_layer

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object FurnitureDataSource {
    var furnitureObjects: List<FurnitureDataModel> = emptyList()


    suspend fun fetchAllFurnitureData(): List<FurnitureDataModel> {
        val firestore = FirebaseFirestore.getInstance()
        try {
            var furnitureList: List<FurnitureDataModel> = emptyList()
            var snapshot = firestore.collection("bathroom_rules").get().await()
            furnitureList = furnitureList + snapshot.documents.mapNotNull { doc ->
                val furnitureType = doc.getString("type") ?: ""
    //            val direction = doc.getString("favorableDirections") ?: ""
                val direction = doc.get("favorableDirections") as? List<String>
                val benefits = doc.getString("reasonfavorable") ?: ""
                val avoid = doc.getString("reasonUnfavorable") ?: ""
    //            val room = doc.getString("room") ?: ""

                FurnitureDataModel(
                    furnitureType = furnitureType,
                    direction = direction!!,
                    benefits = benefits,
                    avoid = avoid,
                    room = "Bathroom"
                )
            }
            snapshot = firestore.collection("kitchen_rules").get().await()
            furnitureList = furnitureList + snapshot.documents.mapNotNull { doc ->
                val furnitureType = doc.getString("type") ?: ""
                //            val direction = doc.getString("favorableDirections") ?: ""
                val direction = doc.get("favorableDirections") as? List<String>
                val benefits = doc.getString("reasonfavorable") ?: ""
                val avoid = doc.getString("reasonUnfavorable") ?: ""
                //            val room = doc.getString("room") ?: ""
                FurnitureDataModel(
                    furnitureType = furnitureType,
                    direction = direction!!,
                    benefits = benefits,
                    avoid = avoid,
                    room = "Kitchen"
                )
            }
            snapshot = firestore.collection("furniture_rules").get().await()
            furnitureList = furnitureList + snapshot.documents.mapNotNull { doc ->
                val furnitureType = doc.getString("type") ?: ""
                //            val direction = doc.getString("favorableDirections") ?: ""
                val direction = doc.get("favorableDirections") as? List<String>
                val benefits = doc.getString("reasonfavorable") ?: ""
                val avoid = doc.getString("reasonUnfavorable") ?: ""
                //            val room = doc.getString("room") ?: ""
                FurnitureDataModel(
                    furnitureType = furnitureType,
                    direction = direction!!,
                    benefits = benefits,
                    avoid = avoid,
                    room = "Bedroom"
                )
            }
            return furnitureList
        } catch (e: Exception) {
            e.printStackTrace()
            return emptyList()
        }

//        return try {
//            val snapshot = firestore.collection(collectionName).get().await()
//            snapshot.documents.mapNotNull { doc ->
//                val furnitureType = doc.getString("object") ?: ""
//                val direction = doc.getString("recommended_direction") ?: ""
//                val benefits = doc.getString("benefit") ?: ""
//                val avoid = doc.getString("avoid") ?: ""
//                val room = doc.getString("room") ?: ""
//                FurnitureDataModel(
//                    furnitureType = furnitureType,
//                    direction = direction,
//                    benefits = benefits,
//                    avoid = avoid,
//                    room = room
//                )
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//            emptyList()
//        }
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
//
//return try {
//    val snapshot = firestore.collection(collectionName).get().await()
//    snapshot.documents.mapNotNull { doc ->
//        val furnitureType = doc.getString("object") ?: ""
//        val direction = doc.getString("recommended_direction") ?: ""
//        val benefits = doc.getString("benefit") ?: ""
//        val avoid = doc.getString("avoid") ?: ""
//        val room = doc.getString("room") ?: ""
//        FurnitureDataModel(
//            furnitureType = furnitureType,
//            direction = direction,
//            benefits = benefits,
//            avoid = avoid,
//            room = room
//        )
//    }
//} catch (e: Exception) {
//    e.printStackTrace()
//    emptyList()
//}