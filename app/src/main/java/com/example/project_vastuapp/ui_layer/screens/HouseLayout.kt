@file:OptIn(ExperimentalLayoutApi::class)

package com.example.project_vastuapp.ui_layer.screens


import androidx.compose.foundation.BorderStroke
import androidx.navigation.NavController
import com.example.project_vastuapp.ui.theme.DarkText
import com.example.project_vastuapp.ui.theme.LightAccent
import com.example.project_vastuapp.ui.theme.PrimaryGreen
import com.example.project_vastuapp.ui.theme.PrimaryTanBrown
import androidx.compose.runtime.*
import androidx.compose.ui.text.style.TextAlign
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project_vastuapp.ui.theme.BackgroundBeige

// 2. Data for the screen
val vastuData = mapOf(
    "North East" to listOf("Pooja room", "Study room", "Children's room"),
    "East" to listOf("Living room", "Study Room"),
    "South East" to listOf("Kitchen", "Storage area"),
    "South" to listOf("Storage room", "Puja room (if space is limited)"),
    "South West" to listOf("Master Bedroom", "Guest Room"),
    "West" to listOf("Children's Playground", "Living Room"),
    "North West" to listOf("Staircase", "Storage"),
    "North" to listOf("Study Room", "Pooja room", "Office space")
)

// List of special, clickable rooms
val clickableRooms = setOf(
    "rrrr", "aaaa"
//    "Living room", "Master Bedroom", "Guest Room", "Kitchen", "Office space", "Study Room"
)

data class ZoneGrid(
    val grid: List<List<String>>
)

fun generateVastuGrid(northAngle: Double): ZoneGrid {
    // 1. Normalize angle to 0-360
    val angle = (northAngle % 360 + 360) % 360

    // 2. Calculate how many 45° shifts (with 22.5° tolerance)
    val shiftCount = if (angle <= 22.5 || angle > 337.5) {
        0
    } else {
        ((angle + 22.5) / 45).toInt() % 8
    }

    // 3. Base order (clockwise starting from North)
    val directions = mutableListOf("N", "NE", "E", "SE", "S", "SW", "W", "NW")

    // 4. Rotate clockwise by shiftCount
    repeat(shiftCount) {
        val last = directions.removeLast()
        directions.add(0, last)
    }

    // 5. Build 3x3 grid (center always same)
    val grid = listOf(
        listOf(directions[7], directions[0], directions[1]), // top row: NW, N, NE
        listOf(directions[6], "C", directions[2]),           // middle row: W, C, E
        listOf(directions[5], directions[4], directions[3])  // bottom row: SW, S, SE
    )

    return ZoneGrid(grid)
}

@Composable
fun HouseLayouScreen(navController: NavController, northAngle: Double) {

    val grid = generateVastuGrid(northAngle).grid

    // A parent box to place the content at the bottom
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryGreen.copy(alpha = 0.5f)), // Semi-transparent background
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(Modifier.fillMaxSize()) {

            VastuGridScreen(grid)

            // The main content area that takes up the bottom 2/3 of the screen
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight() // Takes 2/3 of the screen height
//                    .shadow(elevation = 16.dp, shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
//                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .background(BackgroundBeige)
            ) {
                // Scrollable list for the Vastu data
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    vastuData.forEach { (direction, rooms) ->
                        // Sticky header for each direction
                        item {
                            DirectionHeader(title = direction)
                        }
                        // List of room cards for the current direction
                        items(rooms) { roomName ->
                            RoomCard(roomName = roomName)
                        }
                    }
                }
            }
        }

    }
}

@Composable
fun DirectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = PrimaryGreen,
        modifier = Modifier
            .padding(bottom = 8.dp, top = 16.dp)
    )
}

@Composable
fun RoomCard(roomName: String) {
    val context = LocalContext.current
    val isClickable = clickableRooms.contains(roomName)

    val cardModifier = if (isClickable) {
        Modifier.clickable {
            // Handle navigation or show a toast message
            Toast.makeText(context, "Navigating to $roomName details...", Toast.LENGTH_SHORT).show()
        }
    } else {
        Modifier
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(cardModifier), // Apply clickable modifier
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        // Add a distinct border for clickable items
        border = if (isClickable) BorderStroke(1.5.dp, PrimaryTanBrown) else null
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = roomName,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = DarkText,
                modifier = Modifier.weight(1f)
            )
            // Show trailing icon only for clickable cards
            if (isClickable) {
                Spacer(modifier = Modifier.width(16.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = "Navigate",
                    tint = LightAccent,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}


@Composable
fun VastuGridScreen(grid: List<List<String>>) {
    // 🎨 Color Palette
    val primaryBase = Color(0xFF5B8077)
    val primaryAccent = Color(0xFFB47D64)
    val backgroundColor = Color(0xFFF5F0E8)
    val textColor = Color(0xFF333333)
    val secondaryAccent = Color(0xFFD9AC7A)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(backgroundColor)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        // 🟩 The 3×3 grid occupying about 1/3 of the screen height
        Column(
            modifier = Modifier
                .fillMaxWidth(0.60f)
                .fillMaxHeight(0.60f)
                .aspectRatio(1f) // keeps it square
                .background(primaryBase.copy(alpha = 0.15f))
        ) {
            for (row in grid) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    for (zone in row) {
                        ZoneCell(zone, primaryBase, primaryAccent, textColor, secondaryAccent, modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
fun ZoneCell(
    zone: String,
    primaryBase: Color,
    primaryAccent: Color,
    textColor: Color,
    secondaryAccent: Color,
    modifier: Modifier = Modifier
) {
    val bgColor = when (zone) {
        "N" -> primaryBase.copy(alpha = 0.3f)
        "S" -> primaryAccent.copy(alpha = 0.3f)
        "E" -> secondaryAccent.copy(alpha = 0.3f)
        "W" -> secondaryAccent.copy(alpha = 0.25f)
        "NE", "NW", "SE", "SW" -> primaryBase.copy(alpha = 0.15f)
        "C" -> Color.White.copy(alpha = 0.7f)
        else -> Color.LightGray
    }

    Box(
        modifier = modifier
            .fillMaxHeight()
            .background(bgColor)
            .padding(2.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = zone,
            color = textColor,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
    }
}
//
//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun VastuRecommendationsScreenPreview() {
//    VastuRecommendationsScreen()
//}