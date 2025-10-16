package com.example.project_vastuapp.ui_layer.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Chair
import androidx.compose.material.icons.filled.Dangerous
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TipsAndUpdates
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.project_vastuapp.ui.theme.ColorBrown
import com.example.project_vastuapp.ui.theme.ColorWhite
import com.example.project_vastuapp.ui.theme.ColorDarkOlive
import com.example.project_vastuapp.ui.theme.ColorSageGreen
import com.example.project_vastuapp.ui.theme.ColorDarkGray
import com.example.project_vastuapp.ui.theme.ColorLightBeige
import com.example.project_vastuapp.ui.theme.ColorSand
import com.example.project_vastuapp.ui.theme.ColorTerracotta
import com.example.project_vastuapp.ui_layer.state.VastuTipsViewModel

// --- 2. Home Screen (Left UI) ---
@Composable
fun HomeScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorWhite)
    ) {
        // Organic Wave Background at the bottom
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(520.dp)
                .align(Alignment.BottomCenter)
        ) {
            val width = size.width
            val height = size.height

            val path = Path().apply {
                moveTo(0f, height * 0.7f)
                cubicTo(
                    width * 0.3f, height * 0.85f,
                    width * 0.7f, height * 0.5f,
                    width, height * 0.6f
                )
                lineTo(width, height)
                lineTo(0f, height)
                close()
            }
            drawPath(path, ColorDarkOlive)

            val path2 = Path().apply {
                moveTo(0f, height * 0.6f)
                cubicTo(
                    width * 0.3f, height * 0.7f,
                    width * 0.6f, height * 0.4f,
                    width, height * 0.5f
                )
                lineTo(width, height)
                lineTo(0f, height)
                close()
            }
            drawPath(path2, ColorSageGreen)
        }

        Column(
            modifier = Modifier
                .fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.width(8.dp))
                Text("Vastu Ratan", style = MaterialTheme.typography.headlineLarge)
            }

            Spacer(modifier = Modifier.height(150.dp))

            // --- Grid of Buttons ---
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Row 1
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    // Note: Using a placeholder icon for simplicity
                    VastuGridButton("House Planner", Icons.Default.Home, ColorSand, onClick = {navController.navigate("Compass")})
                    VastuGridButton("Room Setup", Icons.Default.MeetingRoom, ColorBrown, onClick = {navController.navigate("Room")})
                    VastuGridButton("Furniture", Icons.Default.Chair, ColorSageGreen, onClick = {navController.navigate("Furniture")})
                }
                Spacer(modifier = Modifier.height(48.dp))
                // Row 2
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    VastuGridButton("Know Vastu", Icons.Default.Info, ColorSand, onClick = {navController.navigate("VastuIntro")})
                    VastuGridButton("Vastu Tips", Icons.Default.TipsAndUpdates, ColorBrown, onClick = {navController.navigate("VastuTips")}) // Duplicate for design
                    VastuGridButton("Geeta Teachings", Icons.Default.MenuBook, ColorSageGreen, onClick = {navController.navigate("Geeta")}) // Duplicate for design
                }
            }


            Spacer(modifier = Modifier.height(210.dp))

            // --- Daily Vastu Tip Card ---
            DailyVastuTipCard()
        }
    }
}

// Reusable Composable for Grid Buttons
@Composable
fun RowScope.VastuGridButton(text: String, icon: ImageVector, iconBackground: Color, onClick: () -> Unit = {}) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.weight(1f)
    ) {
        Card(
            modifier = Modifier.size(72.dp).clickable(onClick = {onClick()}),
            elevation = CardDefaults.cardElevation(0.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = iconBackground),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 19.dp).size(32.dp),
                tint = ColorWhite
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = text,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            color = ColorDarkGray
        )
    }
}

// Composable for the Daily Tip Card
@Composable
fun DailyVastuTipCard(tipsViewModel: VastuTipsViewModel  = viewModel()) {
    Card(
        modifier = Modifier.fillMaxWidth().background(Color.Transparent),
        elevation = CardDefaults.cardElevation(0.dp),
//        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Column(
            modifier = Modifier.padding(24.dp).background(Color.Transparent)
        ) {


            // Content below the accent area
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Daily Vastu Tip",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                tipsViewModel.getDailyTip(),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(16.dp))
//            Button(
//                onClick = { /* Handle "Read More" */ },
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = ColorDarkOlive),
//                modifier = Modifier.align(Alignment.End),
//                shape = RoundedCornerShape(12.dp)
//            ) {
//                Text("READ MORE", color = ColorWhite)
//            }
        }
    }
}

//// --- Preview Function ---
//@Preview(showBackground = true)
//@Composable
//fun PreviewVastuApp() {
//    VastuAppTheme {
//        val navController = rememberNavController()
//        // Display both screens side-by-side for comparison
//        Row(modifier = Modifier.fillMaxWidth()) {
//            Box(modifier = Modifier.weight(1f)) {
//                HomeScreen(navController)
//            }
//            Box(modifier = Modifier.weight(1f)) {
//                MenuScreen(navController)
//            }
//        }
//    }
//}