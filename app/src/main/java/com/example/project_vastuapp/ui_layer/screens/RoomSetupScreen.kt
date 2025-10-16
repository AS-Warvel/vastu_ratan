package com.example.project_vastuapp.ui_layer.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.project_vastuapp.ui.theme.BackgroundBeige
import com.example.project_vastuapp.ui.theme.DarkText
import com.example.project_vastuapp.ui.theme.LightAccent
import com.example.project_vastuapp.ui.theme.PrimaryGreen
import com.example.project_vastuapp.ui.theme.PrimaryTanBrown
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.json.JSONArray
import java.util.UUID
import com.example.project_vastuapp.ui_layer.state.RoomViewModel
import com.example.project_vastuapp.ui_layer.state.vastuJsonData
import com.example.project_vastuapp.ui_layer.state.RoomUiState
import com.example.project_vastuapp.ui_layer.state.RoomFurnitureItem
import com.example.project_vastuapp.ui_layer.state.getIconForFurniture


// --- 5. UI SCREEN 1: INSPECTION SCREEN ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomInspectionScreen(navController: NavController, viewModel: RoomViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Vastu Inspector", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = BackgroundBeige,
                    titleContentColor = PrimaryGreen
                )
            )
        },
        bottomBar = {
            Button(
                onClick = { navController.navigate("Results")
                          },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryTanBrown),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Confirm & View Results", fontSize = 16.sp)
            }
        },
        containerColor = BackgroundBeige
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            // Room Selection Chips
            RoomSelector2(
                rooms = uiState.rooms,
                selectedRoom = uiState.selectedRoom,
                onRoomSelected = { viewModel.selectRoom(it) }
            )

            // Furniture List
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.itemsForSelectedRoom, key = { it.id }) { item ->
                    FurnitureInspectionCard(
                        item = item,
                        directionOptions = viewModel.getDirectionOptions(),
                        onDirectionChange = { newDirection ->
                            viewModel.updateDirection(item.id, newDirection)
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomSelector2(rooms: List<String>, selectedRoom: String, onRoomSelected: (String) -> Unit) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(rooms) { room ->
            FilterChip(
                selected = room == selectedRoom,
                onClick = { onRoomSelected(room) },
                label = { Text(room) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = LightAccent,
                    selectedLabelColor = DarkText,
                    containerColor = Color.White
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = room == selectedRoom,
                    selected = room == selectedRoom,
                    borderColor = PrimaryGreen,
                    selectedBorderColor = Color.Transparent
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FurnitureInspectionCard(
    item: RoomFurnitureItem,
    directionOptions: List<String>,
    onDirectionChange: (String) -> Unit
) {
    var isDropdownExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Furniture Image and Name
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Image(
                    imageVector =  item.icon,
//                    imageVector = ImageVector.vectorResource(id = getIconForFurniture(item.furnitureType)),
                    contentDescription = item.furnitureType,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(item.furnitureType, fontWeight = FontWeight.SemiBold, fontSize = 18.sp, color = DarkText)
            }

            // Direction Dropdown
            ExposedDropdownMenuBox(
                expanded = isDropdownExpanded,
                onExpandedChange = { isDropdownExpanded = it }
            ) {
                OutlinedTextField(
                    value = item.currentSelectedDirection,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDropdownExpanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .width(150.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryGreen,
                        unfocusedBorderColor = Color.Gray,
                    )
                )
                ExposedDropdownMenu(
                    expanded = isDropdownExpanded,
                    onDismissRequest = { isDropdownExpanded = false }
                ) {
                    directionOptions.forEach { direction ->
                        DropdownMenuItem(
                            text = { Text(direction) },
                            onClick = {
                                onDirectionChange(direction)
                                isDropdownExpanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}


