package com.example.project_vastuapp.ui_layer.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project_vastuapp.R
import com.example.project_vastuapp.ui.theme.black
import com.example.project_vastuapp.ui.theme.buttonContainer
import com.example.project_vastuapp.ui.theme.vastuLogoColor

@Composable
fun CheckVastuButton(enableAddFurnitureScreen : () -> Unit) {
    Button(modifier = Modifier.height(100.dp).width(300.dp) ,onClick = {enableAddFurnitureScreen()},
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonContainer,
            contentColor = black,
        )) {
            Text("Check Your Vastu", fontSize = 24.sp)
    }
}

@Composable
fun VastuLogo() {
    Icon(
        painter = painterResource(id = R.drawable.vastu_ratan_logo),
        tint = vastuLogoColor,
        contentDescription = "Vastu Ratan App Logo"
    )
}

@Composable
fun WelcomeBanner() {
    Card(modifier = Modifier.height(100.dp).fillMaxWidth().padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth().height(100.dp).background(Color.Cyan),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Welcome to Vastu Ratan",
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}