import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project_vastuapp.ui.theme.black
import com.example.project_vastuapp.ui.theme.dialogBoxButtonsColor
import kotlin.reflect.KClass

@Composable
fun SelectionInputField(
    value: String,
    inputSelectionList: List<String>,
    onValueSelection: (String) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier.fillMaxWidth()
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded }
            .clip(RoundedCornerShape(15.dp))
            .background(Color.White)
            .padding(start = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(modifier = Modifier.weight(1f).padding(top = 4.dp),text = value, fontSize = 16.sp, color = Color.DarkGray)
        IconButton(
            onClick = {
                isExpanded = !isExpanded
            }
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = "Furniture Selection"
            )

            DropdownMenu(
                expanded = isExpanded,
                onDismissRequest = {isExpanded = false}
            ) {
                inputSelectionList.forEach {
                    DropdownMenuItem(text = {Text(it)}, onClick = {
                        onValueSelection(it)
                        isExpanded = false
                    })
                }
            }
        }

    }
}

@Composable
fun ConfirmButton(onClick :() -> Unit, enabled: Boolean) {
    Button(modifier = Modifier.height(40.dp).width(110.dp),
        onClick = {
            onClick()
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = dialogBoxButtonsColor,
            contentColor = black
        ),
        enabled = enabled) {
        Text("Confirm", fontSize = 16.sp)
    }
}

@Composable
fun CancelButton(onClick :() -> Unit) {
    Button(modifier = Modifier.height(40.dp).width(100.dp),
        onClick = {
            onClick()
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = dialogBoxButtonsColor,
            contentColor = black
        )) {
            Text("Cancel", fontSize = 16.sp)
        }
}