//package com.example.project_vastuapp.ui_layer.screens
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.RowScope
//import androidx.compose.foundation.layout.aspectRatio
//import androidx.compose.foundation.layout.fillMaxHeight
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Chair
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.Icon
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.example.project_vastuapp.ui_layer.state.FurnitureInspectionDataSource
//
//// 🎨 Color Palette
//val primaryBase = Color(0xFF5B8077)
//val primaryAccent = Color(0xFFB47D64)
//val backgroundColor = Color(0xFFF5F0E8)
//val textColor = Color(0xFF333333)
//val secondaryAccent = Color(0xFFD9AC7A)
//
//
//
//// Main Screen Composable
//@Composable
//fun VastuPlacementScreen() {
//    // 💾 State Management
//    val vastuObjects = remember { FurnitureInspectionDataSource.getObjectsForPlacement() }
//    val grid = remember {
//        listOf(
//            listOf("NW", "N", "NE"),
//            listOf("W", "C", "E"),
//            listOf("SW", "S", "SE")
//        )
//    }
//    var selectedZone by remember { mutableStateOf("C") }
//
//    // 🔄 Derived state for filtering the list based on the selected zone
//    val filteredList = remember(selectedZone, vastuObjects) {
//        if (selectedZone.isEmpty()) {
//            emptyList()
//        } else {
//            vastuObjects.filter {
//                getZonesFromDirection(it.recommendedDirection).contains(selectedZone)
//            }
//        }
//    }
//
//    // 🖼️ UI Layout
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(backgroundColor)
//            .padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.spacedBy(24.dp)
//    ) {
//        Text(
//            text = "Vastu Placement Guide",
//            style = MaterialTheme.typography.headlineSmall,
//            color = textColor,
//            fontWeight = FontWeight.Bold
//        )
//
//        VastuGrid(
//            grid = grid,
//            selectedZone = selectedZone,
//            onZoneSelected = { zone -> selectedZone = zone }
//        )
//
//        Text(
//            text = "Recommended for ${getZoneFullName(selectedZone)} Zone",
//            style = MaterialTheme.typography.titleLarge,
//            color = textColor,
//            fontWeight = FontWeight.SemiBold
//        )
//
//        FurnitureList(items = filteredList)
//    }
//}
//
//// Composable for the 3x3 Grid
//@Composable
//fun VastuGrid(
//    grid: List<List<String>>,
//    selectedZone: String,
//    onZoneSelected: (String) -> Unit
//) {
//    Column(
//        modifier = Modifier
//            .fillMaxWidth(0.9f)
//            .aspectRatio(1f)
//            .background(primaryBase.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
//            .border(1.dp, primaryBase.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
//    ) {
//        grid.forEach { row ->
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .weight(1f)
//            ) {
//                row.forEach { zone ->
//                    ZoneCell(
//                        zone = zone,
//                        isSelected = zone == selectedZone,
//                        onClick = { onZoneSelected(zone) }
//                    )
//                }
//            }
//        }
//    }
//}
//
//// Composable for a single selectable cell in the grid
//@Composable
//fun RowScope.ZoneCell(
//    zone: String,
//    isSelected: Boolean,
//    onClick: () -> Unit
//) {
//    val bgColor = when (zone) {
//        "N" -> primaryBase.copy(alpha = 0.3f)
//        "S" -> primaryAccent.copy(alpha = 0.3f)
//        "E" -> secondaryAccent.copy(alpha = 0.3f)
//        "W" -> secondaryAccent.copy(alpha = 0.25f)
//        "NE", "NW", "SE", "SW" -> primaryBase.copy(alpha = 0.15f)
//        "C" -> Color.White.copy(alpha = 0.7f)
//        else -> Color.LightGray
//    }
//
//    // Highlight selected cell with a border
//    val borderModifier = if (isSelected) {
//        Modifier.border(2.5.dp, primaryAccent, RoundedCornerShape(8.dp))
//    } else {
//        Modifier.border(1.dp, primaryBase.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
//    }
//
//    Box(
//        modifier = Modifier
//            .weight(1f)
//            .fillMaxHeight()
//            .padding(4.dp)
//            .then(borderModifier)
//            .background(bgColor, RoundedCornerShape(8.dp))
//            .clip(RoundedCornerShape(8.dp))
//            .clickable(onClick = onClick),
//        contentAlignment = Alignment.Center
//    ) {
//        Text(
//            text = zone,
//            color = textColor,
//            fontSize = 18.sp,
//            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
//            textAlign = TextAlign.Center
//        )
//    }
//}
//
//// Composable for the list of furniture items
//@Composable
//fun FurnitureList(items: List<VastuObject>) {
//    if (items.isEmpty()) {
//        Box(
//            modifier = Modifier.fillMaxSize(),
//            contentAlignment = Alignment.Center
//        ) {
//            Text(
//                "No specific items found for this zone.",
//                color = textColor.copy(alpha = 0.7f),
//                style = MaterialTheme.typography.bodyLarge,
//                textAlign = TextAlign.Center
//            )
//        }
//    } else {
//        LazyColumn(
//            verticalArrangement = Arrangement.spacedBy(12.dp),
//            contentPadding = PaddingValues(bottom = 16.dp)
//        ) {
//            items(items) { item ->
//                FurnitureCard(item = item)
//            }
//        }
//    }
//}
//
//// Composable for a single furniture item card
//@Composable
//fun FurnitureCard(item: VastuObject) {
//    Card(
//        modifier = Modifier.fillMaxWidth(),
//        shape = RoundedCornerShape(12.dp),
//        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
//        colors = CardDefaults.cardColors(containerColor = Color.White)
//    ) {
//        Row(
//            modifier = Modifier
//                .padding(16.dp)
//                .fillMaxWidth(),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
//            // Placeholder Icon
//            Icon(
//                imageVector = Icons.Filled.Chair,
//                contentDescription = item.`object`,
//                modifier = Modifier
//                    .size(40.dp)
//                    .background(primaryBase.copy(alpha = 0.1f), CircleShape)
//                    .padding(8.dp),
//                tint = primaryBase
//            )
//            // Furniture Name
//            Text(
//                text = item.`object`,
//                style = MaterialTheme.typography.titleMedium,
//                fontWeight = FontWeight.SemiBold,
//                color = textColor
//            )
//        }
//    }
//}
//
//
//// ⚙️ Helper Functions
///**
// * Parses a direction string (e.g., "South or West wall") and returns a list
// * of corresponding zone codes (e.g., ["S", "W"]).
// */
//fun getZonesFromDirection(direction: String): List<String> {
//    val zones = mutableListOf<String>()
//    val lowerCaseDirection = direction.lowercase()
//
//    if (lowerCaseDirection.contains("north-east")) zones.add("NE")
//    if (lowerCaseDirection.contains("north-west")) zones.add("NW")
//    if (lowerCaseDirection.contains("south-east")) zones.add("SE")
//    if (lowerCaseDirection.contains("south-west")) zones.add("SW")
//    if (lowerCaseDirection.contains("north") && !zones.any { it.contains("N") }) zones.add("N")
//    if (lowerCaseDirection.contains("south") && !zones.any { it.contains("S") }) zones.add("S")
//    if (lowerCaseDirection.contains("east") && !zones.any { it.contains("E") }) zones.add("E")
//    if (lowerCaseDirection.contains("west") && !zones.any { it.contains("W") }) zones.add("W")
//    if (lowerCaseDirection.contains("center") || lowerCaseDirection.contains("brahmasthan")) zones.add("C")
//
//    return zones.distinct()
//}
//
///**
// * Converts a zone code (e.g., "NE") to its full name (e.g., "North-East").
// */
//fun getZoneFullName(zone: String): String {
//    return when (zone) {
//        "N" -> "North"
//        "S" -> "South"
//        "E" -> "East"
//        "W" -> "West"
//        "NE" -> "North-East"
//        "NW" -> "North-West"
//        "SE" -> "South-East"
//        "SW" -> "South-West"
//        "C" -> "Center"
//        else -> "Selected"
//    }
//}