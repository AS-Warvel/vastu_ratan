package com.example.project_vastuapp.datalayer

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader

fun loadJSONFromRaw(context: Context, resourceId: Int): String {
    val inputStream = context.resources.openRawResource(resourceId)
    val reader = BufferedReader(InputStreamReader(inputStream))
    val stringBuilder = StringBuilder()
    var line: String?
    while (reader.readLine().also { line = it } != null) {
        stringBuilder.append(line)
    }
    reader.close()
    return stringBuilder.toString()
}