package com.example.project_vastuapp.ui_layer.widgets.reusable_components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.example.project_vastuapp.R
import com.example.project_vastuapp.ui.theme.background
import com.example.project_vastuapp.ui.theme.black

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(showActions: Boolean = false) {
    val thephirFont = FontFamily(Font(R.font.thephir_bold))

    CenterAlignedTopAppBar(
        title = { Text("Vastu Ratan", fontFamily = thephirFont, fontSize = 42.sp)},
        modifier = Modifier.fillMaxWidth(),
        colors = TopAppBarColors(containerColor = background, titleContentColor = black, scrolledContainerColor = background, actionIconContentColor = black, navigationIconContentColor = background),
        actions = {
            if(showActions) {
                AppBarActions()
            }
        },
    )
}