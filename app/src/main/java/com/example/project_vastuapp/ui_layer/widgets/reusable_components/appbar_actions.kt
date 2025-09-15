package com.example.project_vastuapp.ui_layer.widgets.reusable_components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun AppBarActions() {
    var expanded by remember { mutableStateOf(false) }
    IconButton(onClick = {expanded = !expanded}) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = "Tob Bar Menu Button"
        )
    }
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = {expanded = false},
    ) {
        DropdownMenuItem(text = {Text("Vastu Tips")}, onClick = {})
        DropdownMenuItem(text = {Text("Motivational Quotes")}, onClick = {})
        DropdownMenuItem(text = {Text("About")}, onClick = {})
    }

}