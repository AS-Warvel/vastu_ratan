package com.example.project_vastuapp.ui_layer.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.project_vastuapp.ui.theme.PrimaryGreen
import com.example.project_vastuapp.ui.theme.PrimaryTanBrown
import com.example.project_vastuapp.ui.theme.BackgroundBeige
import com.example.project_vastuapp.ui.theme.DarkText
import com.example.project_vastuapp.ui.theme.LightAccent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import com.example.project_vastuapp.ui.theme.ColorDarkOlive
import com.example.project_vastuapp.ui.theme.ColorSageGreen

@ExperimentalMaterial3Api
@Composable
fun VastuIntroScreen(navController: NavController) {
    Scaffold(

//        containerColor = BackgroundBeige,
//        backgroundColor = BackgroundBeige
    ) { paddingValues ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Introduction to Vastu", style = MaterialTheme.typography.headlineLarge)
                }
                Spacer(modifier = Modifier.height(16.dp))

                // --- Introduction Section ---
                Text(
                    text = "Vastu Shastra is an ancient Indian science of architecture that seeks to create harmony between people and their environment. It unifies science, art, and astrology to design living and working spaces that maximize positivity, health, and prosperity.",
                    color = DarkText,
                    fontSize = 16.sp,
                    lineHeight = 24.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "The core idea is to build structures that align with natural forces, much like how animals instinctively build their homes in a specific way for survival. By following Vastu principles, a home or office can become a place that nurtures well-being.",
                    color = DarkText,
                    fontSize = 16.sp,
                    lineHeight = 24.sp
                )

                Spacer(modifier = Modifier.height(32.dp))

                // --- Core Principles Section ---
                Text(
                    text = "Core Principles",
                    color = PrimaryGreen,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Divider(
                    color = PrimaryGreen.copy(alpha = 0.5f),
                    thickness = 1.dp,
                    modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                )
                Text(
                    text = "Vastu is built on balancing two key components: the five elements and the eight directions.",
                    color = DarkText,
                    fontSize = 16.sp,
                    lineHeight = 24.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                // --- The Five Elements ---
                Text(
                    text = "The Five Elements (Panchabhutas)",
                    color = DarkText,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "The universe is composed of five basic elements, and Vastu seeks to harmonize them within a structure.",
                    color = DarkText,
                    fontSize = 16.sp,
                    lineHeight = 24.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                // Elements List
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    ElementDescription("Earth (Bhumi):", "Represents stability and foundation.")
                    ElementDescription("Water (Jal):", "Symbolizes flow and prosperity.")
                    ElementDescription(
                        "Fire (Agni):",
                        "Relates to energy, heat, and transformation."
                    )
                    ElementDescription("Air (Vayu):", "Governs movement and communication.")
                    ElementDescription("Space (Akasha):", "Represents openness and potential.")
                }

                Spacer(modifier = Modifier.height(24.dp))

                // --- The Eight Directions ---
                Text(
                    text = "The Eight Directions",
                    color = DarkText,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Vastu assigns each element and aspect of life to a specific direction (the four cardinal and four inter-cardinal points). Proper alignment ensures that the energy of each direction is harnessed correctly, promoting success and preventing negative influences.",
                    color = DarkText,
                    fontSize = 16.sp,
                    lineHeight = 24.sp
                )
                Spacer(modifier = Modifier.height(20.dp))

                // --- Image Placeholder ---
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(PrimaryGreen.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Image Placeholder",
                        color = PrimaryGreen,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // --- Why Vastu Matters Section ---
                Text(
                    text = "Why Vastu Matters",
                    color = PrimaryGreen,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Divider(
                    color = PrimaryGreen.copy(alpha = 0.5f),
                    thickness = 1.dp,
                    modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                )
                Text(
                    text = "Think of Vastu as the geometry for a positive life. Just as your body posture can affect your health, the geometry, orientation, and design of your home significantly impact the energy within it.",
                    color = DarkText,
                    fontSize = 16.sp,
                    lineHeight = 24.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "By applying Vastu, you can create a balanced and congenial environment. The goal is to take advantage of the benefits bestowed by nature, paving the way for enhanced health, wealth, and happiness in an enlightened space. These principles can be applied to any building, from homes and offices to temples and entire cities.",
                    color = DarkText,
                    fontSize = 16.sp,
                    lineHeight = 24.sp
                )
            }


    }
}

@Composable
fun ElementDescription(name: String, description: String) {
    Text(
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = DarkText)) {
                append("• $name ")
            }
            withStyle(style = SpanStyle(color = DarkText)) {
                append(description)
            }
        },
        fontSize = 16.sp,
        lineHeight = 24.sp
    )
}

//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    VastuIntroScreen()
//}