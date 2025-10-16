package com.example.project_vastuapp.ui.theme.VastuAppTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project_vastuapp.ui.theme.ColorDarkOlive
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.example.project_vastuapp.ui.theme.ColorDarkGray
import com.example.project_vastuapp.ui.theme.ColorWhite
import com.example.project_vastuapp.ui.theme.PrimaryGreen
import com.example.project_vastuapp.ui.theme.LightAccent
import com.example.project_vastuapp.ui.theme.BackgroundBeige
import com.example.project_vastuapp.ui.theme.DarkText
import com.example.project_vastuapp.ui.theme.PrimaryTanBrown





@Composable
fun VastuAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
//            primary = ColorDarkOlive,
//            surface = ColorWhite,
//            background = ColorWhite
            primary = PrimaryGreen,
            secondary = LightAccent,
            background = BackgroundBeige,
            surface = BackgroundBeige,
            onPrimary = Color.White,
            onSecondary = DarkText,
            onBackground = DarkText,
            onSurface = DarkText,
            primaryContainer = PrimaryTanBrown,
            onPrimaryContainer = Color.White
        ),
        typography = Typography(
            headlineMedium = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = ColorDarkOlive
            ),
            bodyMedium = TextStyle(
                fontSize = 16.sp,
                color = ColorDarkGray
            )
        ),
        shapes = Shapes(
            medium = RoundedCornerShape(16.dp)
        ),
        content = content
    )
}