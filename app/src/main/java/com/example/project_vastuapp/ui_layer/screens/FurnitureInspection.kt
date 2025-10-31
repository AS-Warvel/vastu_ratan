package com.example.project_vastuapp.ui_layer.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.project_vastuapp.data_layer.VastuObject
import com.example.project_vastuapp.ui_layer.state.FurnitureViewModel

@Composable
fun FurnitureInspectionScreen(
    navController: NavController,
    furnitureViewModel: FurnitureViewModel = viewModel()
) {
    // Collect states from the ViewModel
    val rooms by remember { mutableStateOf(furnitureViewModel.rooms) }
    val selectedRoom by furnitureViewModel.selectedRoom.collectAsStateWithLifecycle()
    val furnitureList by furnitureViewModel.furnitureForSelectedRoom.collectAsStateWithLifecycle()
    val selectedFurniture by furnitureViewModel.selectedFurniture.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()), // Make the whole screen scrollable
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.width(8.dp))
                Text("Furniture Inspection", style = MaterialTheme.typography.headlineLarge)
            }
            Spacer(modifier = Modifier.height(8.dp))
            // Room Selector Chips
            RoomSelector(
                rooms = rooms,
                selectedRoom = selectedRoom,
                onRoomSelected = { room -> furnitureViewModel.selectRoom(room) }
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Information Card
            Box(Modifier.height(420.dp))
            {
                selectedFurniture?.let {
                    VastuInfoCard(it)
                }
            }
            Spacer(modifier = Modifier.weight(1f))

            // Furniture Icon Selector
            Text(
                text = "Select Furniture",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(8.dp)
            )
            FurnitureSelector(
                furnitureList = furnitureList,
                selectedFurniture = selectedFurniture,
                onFurnitureSelected = { furniture -> furnitureViewModel.selectFurniture(furniture) }
            )
        }
    }
}

@Composable
fun RoomSelector(
    rooms: List<String>,
    selectedRoom: String,
    onRoomSelected: (String) -> Unit
) {
    LazyRow(
        modifier = Modifier.padding(vertical = 12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(rooms) { room ->
            FilterChip(
                selected = room == selectedRoom,
                onClick = { onRoomSelected(room) },
                label = { Text(room, style = MaterialTheme.typography.bodyMedium, color = if(room == selectedRoom) Color.White else MaterialTheme.colorScheme.onSurface) },
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = Color.White
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = room == selectedRoom,
                    selected = room == selectedRoom,
                    borderColor = MaterialTheme.colorScheme.primary,
                    selectedBorderColor = Color.Transparent
                )
            )
        }
    }
}

@Composable
fun FurnitureSelector(
    furnitureList: List<VastuObject>,
    selectedFurniture: VastuObject?,
    onFurnitureSelected: (VastuObject) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(furnitureList) { furniture ->
            val isSelected = furniture == selectedFurniture
            val backgroundColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
            val iconColor = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.secondary

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable { onFurnitureSelected(furniture) }
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .shadow(elevation = 4.dp, shape = CircleShape)
                        .background(backgroundColor, CircleShape)
                        .clip(CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = furniture.icon,
                        contentDescription = furniture.furnitureType,
                        modifier = Modifier.size(32.dp),
                        tint = iconColor
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = furniture.furnitureType,
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.width(70.dp)
                )
            }
        }
    }
}

@Composable
fun VastuInfoCard(furniture: VastuObject) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(bottom = 16.dp, start = 16.dp, end = 16.dp).fillMaxWidth().fillMaxHeight(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Row(Modifier.fillMaxWidth(), Arrangement.Center) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .shadow(elevation = 4.dp, shape = CircleShape)
                        .background(MaterialTheme.colorScheme.surface, CircleShape)
                        .clip(CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = furniture.icon,
                        contentDescription = furniture.furnitureType,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            Row(Modifier.fillMaxWidth(), Arrangement.Center) {
                Text(
                    text = furniture.furnitureType,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            Spacer(modifier = Modifier.height(24.dp))

            InfoSection(
                title = "Right Direction",
                text = furniture.correctDirection,
                reason = furniture.whyThisDirection,
                icon = Icons.Default.CheckCircle,
                iconColor = MaterialTheme.colorScheme.primary
            )

            Divider(modifier = Modifier.padding(vertical = 16.dp))

            // Avoid Direction Section
            furniture.directionsToAvoid?.let {
                InfoSection(
                    title = "Directions to Avoid",
                    text = null,
                    reason = it, // No separate reason for avoidance in the data model
                    icon = Icons.Default.Warning,
                    iconColor = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

@Composable
private fun InfoSection(
    title: String,
    text: String?,
    reason: String?,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color
) {
    Row(verticalAlignment = Alignment.Top) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = iconColor,
            modifier = Modifier
                .padding(end = 12.dp)
                .size(24.dp)
        )
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            text?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            reason?.let {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
            }
        }
    }
}