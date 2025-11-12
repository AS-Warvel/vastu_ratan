package com.example.project_vastuapp.datalayer

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.example.project_vastuapp.R
import com.google.firebase.firestore.toObject

import java.lang.Exception

class VastuRepository(
    private val context: Context,
    private val db: FirebaseFirestore
) {

    // --- existing code kept as-is (getFurnitureRule etc) ---
    interface FurnitureRuleCallback {
        fun onSuccess(rule: FurnitureRule?)
        fun onError(exception: Exception)
    }

    fun getFurnitureRule(furnitureType: String, callback: FurnitureRuleCallback) {
        val typeId = furnitureType.lowercase()

        db.collection("furniture_rules")
            .document(typeId)
            .get()
            .addOnSuccessListener { document ->
                val rule = document.toObject<FurnitureRule>()
                callback.onSuccess(rule)
            }
            .addOnFailureListener { exception ->
                callback.onError(exception)
            }
    }

    fun uploadVastuDataOnce() {
        try {
            db.collection("Rules")
                .limit(1)
                .get()
                .addOnSuccessListener { snap ->
                    if (!snap.isEmpty) {
                        println("⚠️ Rules collection already has documents. Skipping upload.")
                    } else {
                        uploadVastuData()
                    }
                }
                .addOnFailureListener {
                    // If query fails, try uploading anyway
                    uploadVastuData()
                }
        } catch (e: Exception) {
            e.printStackTrace()
            uploadVastuData()
        }
    }

    fun getAllVastuItems(
        onSuccess: (List<VastuItem>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        db.collection("Rules") // collection name used during upload
            .get()
            .addOnSuccessListener { snapshot ->
                val vastuList = snapshot.documents.mapNotNull { doc ->
                    try {
                        doc.toObject(VastuItem::class.java)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        null
                    }
                }
                onSuccess(vastuList)
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }

    /** Uploads all Vastu items from JSON with numeric IDs */
    private fun uploadVastuData() {
        try {
            val gson = Gson()
            val json = loadJSONFromRaw(context, R.raw.vastu)
            val listType = object : TypeToken<List<VastuItem>>() {}.type
            val vastuList: List<VastuItem> = gson.fromJson(json, listType)

            val collectionRef = db.collection("Rules")

            vastuList.forEachIndexed { index, item ->
                val docId = (index + 1).toString() // Numeric ID: 1, 2, 3, ...

                collectionRef.document(docId)
                    .set(item)
                    .addOnSuccessListener {
                        println("✅ Added: $docId -> ${item.`object`}")
                    }
                    .addOnFailureListener { e ->
                        println("❌ Error adding $docId -> ${item.`object`}: ${e.message}")
                    }
            }

            println("📤 Started uploading ${vastuList.size} items to Firestore.")

        } catch (e: Exception) {
            e.printStackTrace()
            println("❌ Error parsing JSON or uploading: ${e.message}")
        }
    }

    /** Helper function to read JSON from res/raw */
    private fun loadJSONFromRaw(context: Context, resId: Int): String {
        return context.resources.openRawResource(resId).bufferedReader().use { it.readText() }
    }
}