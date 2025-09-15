import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.project_vastuapp.ui.theme.background
import com.example.project_vastuapp.ui_layer.widgets.reusable_components.AppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(myViewModel: VastuAppViewModel = viewModel()) {
    var expanded by remember { mutableStateOf(false) }
    Column(Modifier.background(background),horizontalAlignment = Alignment.CenterHorizontally){
        AppBar()

        val furnitureObjects = myViewModel.getFurnitureOutputList()

        LazyColumn(Modifier.weight(0.85f)) {
            items(furnitureObjects) { item ->
                FurnitureListItemCard(item)
            }
        }
        Box(Modifier.weight(0.15f)) {
            GoToHomePageButton(onClick = {myViewModel.navigateTo("Home")})
        }
    }
}