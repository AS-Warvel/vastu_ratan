package com.example.project_vastuapp.ui_layer.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.project_vastuapp.ui_layer.state.VastuTipsViewModel
import androidx.navigation.NavController
import androidx.compose.foundation.layout.height
import com.example.project_vastuapp.ui_layer.state.VastuTip


//@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VastuTipsScreen(vastuTipsViewModel: VastuTipsViewModel = viewModel(), navController: NavController) {

    val selectedCategory by vastuTipsViewModel.selectedCategoryName.collectAsStateWithLifecycle()
    val tips by vastuTipsViewModel.filteredTips.collectAsStateWithLifecycle()
    val categories = vastuTipsViewModel.categories

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.width(8.dp))
                Text("Vastu Tips", style = MaterialTheme.typography.headlineLarge)
            }
            Spacer(modifier = Modifier.height(8.dp))
            CategorySelection(
                categories = categories,
                selectedCategory = selectedCategory,
                onCategorySelected = { category ->
                    vastuTipsViewModel.selectCategory(category)
                }
            )
            VastuTipList(tips = tips)
        }
    }
}

@Composable
fun CategorySelection(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories) { category ->
            FilterChip(
                selected = (category == selectedCategory),
                onClick = { onCategorySelected(category) },
                label = { Text(category) },
                shape = RoundedCornerShape(16.dp),
                colors = FilterChipDefaults.filterChipColors(
                    labelColor = MaterialTheme.colorScheme.onSurface,
                    selectedLabelColor = MaterialTheme.colorScheme.onSecondary,
                    selectedContainerColor = MaterialTheme.colorScheme.secondary,
                    selectedLeadingIconColor = MaterialTheme.colorScheme.onSecondary
                ),
                border = FilterChipDefaults.filterChipBorder(
                    selected = category == selectedCategory,
                    enabled = true,
                    borderColor = MaterialTheme.colorScheme.secondary,
                    borderWidth = 1.dp
                )
            )
        }
    }
}

@Composable
fun VastuTipList(tips: List<VastuTip>) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(tips) { tip ->
            VastuTipCard(tip = tip)
        }
    }
}

@Composable
fun VastuTipCard(tip: VastuTip) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = tip.category.emoji,
                fontSize = 28.sp
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = tip.tip,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 22.sp
            )
        }
    }
}
//
//@Preview(showBackground = true)
//@Composable
//fun VastuScreenPreview() {
//    VastuTipsAppTheme {
//        VastuScreen()
//    }
//}