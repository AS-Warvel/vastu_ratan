package com.example.project_vastuapp.ui_layer.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.project_vastuapp.data_layer.GeetaQuote
import com.example.project_vastuapp.ui_layer.state.GeetaViewModel
import com.example.project_vastuapp.ui.theme.PrimaryGreen
import com.example.project_vastuapp.ui.theme.PrimaryTanBrown
import com.example.project_vastuapp.ui.theme.BackgroundBeige
import com.example.project_vastuapp.ui.theme.DarkText
import com.example.project_vastuapp.ui.theme.LightAccent

@Composable
fun LifeLessonsScreen(navController: NavController,geetaViewModel: GeetaViewModel = viewModel()) {
    // Observe the quotes list from the ViewModel
    val quotes by geetaViewModel.quotes.collectAsStateWithLifecycle()
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("All Quotes", "Favourites")

    Scaffold(
        containerColor = BackgroundBeige,
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
                Text("Geeta Quotes", style = MaterialTheme.typography.headlineLarge)
            }
            Spacer(modifier = Modifier.height(8.dp))

            // TabRow for switching between "All" and "Favourites"
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = BackgroundBeige,
                contentColor = PrimaryGreen,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        height = 3.dp,
                        color = LightAccent // Light Accent for active tab
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title, fontWeight = FontWeight.Bold) }
                    )
                }
            }

            // Content based on selected tab
            when (selectedTabIndex) {
                0 -> QuoteList(
                    quotes = quotes,
                    onToggleFavourite = { geetaViewModel.onFavouriteTapped(it) }
                )
                1 -> QuoteList(
                    quotes = quotes.filter { it.isFavourite },
                    onToggleFavourite = { geetaViewModel.onFavouriteTapped(it) },
                    emptyMessage = "You haven't added any quotes to your favourites yet."
                )
            }
        }
    }
}

@Composable
fun QuoteList(
    quotes: List<GeetaQuote>,
    onToggleFavourite: (GeetaQuote) -> Unit,
    emptyMessage: String? = null
) {
    if (quotes.isEmpty() && emptyMessage != null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = emptyMessage,
                fontSize = 16.sp,
                color = DarkText.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
    } else {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(quotes, key = { it.id }) { quote ->
                QuoteCard(quote = quote, onToggleFavourite = onToggleFavourite)
            }
        }
    }
}

@Composable
fun QuoteCard(quote: GeetaQuote, onToggleFavourite: (GeetaQuote) -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = BackgroundBeige),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "“${quote.quote}”",
                color = DarkText,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            IconButton(onClick = { onToggleFavourite(quote) }) {
                Icon(
                    imageVector = if (quote.isFavourite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "Toggle Favourite",
                    tint = if (quote.isFavourite) PrimaryGreen else DarkText.copy(alpha = 0.7f),
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}