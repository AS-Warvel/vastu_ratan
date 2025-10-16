package com.example.project_vastuapp.ui_layer.screens

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.project_vastuapp.ui.theme.BackgroundBeige
import com.example.project_vastuapp.ui.theme.DarkText
import com.example.project_vastuapp.ui.theme.ErrorRed
import com.example.project_vastuapp.ui.theme.PrimaryGreen
import com.example.project_vastuapp.ui.theme.PrimaryTanBrown
import com.example.project_vastuapp.ui.theme.SuccessGreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.example.project_vastuapp.ui_layer.state.RoomViewModel
import com.example.project_vastuapp.ui_layer.state.RoomUiState
import com.example.project_vastuapp.ui_layer.state.RoomFurnitureItem
import com.example.project_vastuapp.ui_layer.state.getIconForFurniture


// --- 6. UI SCREEN 2: RESULTS SCREEN ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewResultsScreen(navController: NavController, viewModel: RoomViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    print(uiState.allItems)
    val inspectedItems = uiState.allItems.filter { it.currentSelectedDirection != "Not Present" }
    print(inspectedItems)
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Inspection Results", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = BackgroundBeige,
                    titleContentColor = PrimaryGreen
                )
            )
        },
        bottomBar = {
            Button(
                onClick = {
                    navController.popBackStack() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryTanBrown),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Go Back", fontSize = 16.sp)
            }
        },
        containerColor = BackgroundBeige
    ) { paddingValues ->
        if (inspectedItems.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("No items were inspected in this room.",
                    fontSize = 18.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(32.dp)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(paddingValues),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(inspectedItems) { item ->
                    val isCorrect = viewModel.isDirectionCorrect(item)
                    ResultCard(item = item, isCorrect = isCorrect)
                }
            }
        }
    }
}

@Composable
fun ResultCard(item: RoomFurnitureItem, isCorrect: Boolean) {
    var expanded by remember { mutableStateOf(false) }
    val cardColor = if (isCorrect) SuccessGreen.copy(alpha = 0.1f) else ErrorRed.copy(alpha = 0.1f)
    val borderColor = if (isCorrect) SuccessGreen else ErrorRed

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        border = BorderStroke(1.dp, borderColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Collapsed View
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    imageVector = item.icon,
                    contentDescription = item.furnitureType,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = item.furnitureType,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = if (isCorrect) "CORRECT" else "INCORRECT",
                    color = borderColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            // Expanded View
            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn(animationSpec = tween(300)) + slideInVertically(),
                exit = fadeOut(animationSpec = tween(300)) + slideOutVertically()
            ) {
                Column {
                    Divider(
                        modifier = Modifier.padding(vertical = 12.dp),
                        color = borderColor.copy(alpha = 0.5f)
                    )

                    if (isCorrect) {
                        ResultInfoRow(
                            title = "Significance:",
                            details = item.whyThisDirection
                        )
                    } else {
                        ResultInfoRow(
                            title = "Your Direction:",
                            details = item.currentSelectedDirection
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        ResultInfoRow(
                            title = "Recommended:",
                            details = item.correctDirections.joinToString(", ")
                        )
                        item.directionsToAvoid?.let {
                            Spacer(modifier = Modifier.height(8.dp))
                            ResultInfoRow(title = "Directions to Avoid:", details = it)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ResultInfoRow(title: String, details: String) {
    Column {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = DarkText.copy(alpha = 0.8f)
        )
        Text(
            text = details,
            fontSize = 16.sp,
            color = DarkText,
            lineHeight = 22.sp
        )
    }
}
