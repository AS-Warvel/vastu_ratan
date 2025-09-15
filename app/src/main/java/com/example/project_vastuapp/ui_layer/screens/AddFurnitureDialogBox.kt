import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project_vastuapp.ui.theme.black
import com.example.project_vastuapp.ui.theme.dialogBoxButtonsColor
import com.example.project_vastuapp.ui.theme.dialogueBox
import com.example.project_vastuapp.ui.theme.dialogueScreen

//@Preview
@Composable
//fun AddAddFurnitureObjectDialogBox(){
fun AddFurnitureObjectDialogBox(sendFurnitureObjects: (List<FurnitureInput>) -> Unit,
                                cancelOperation: () -> Unit) {

//    var sendFurnitureObjects: (List<FurnitureInput>) -> Unit = {}
//    var cancelOperation: () -> Unit = {}

    var furnitureSelection by remember { mutableStateOf("...") }
    var directionSelection by remember { mutableStateOf("...") }
    var furnitureDropDownSelection by remember { mutableStateOf(false) }
    var directionDropDownSelection by remember { mutableStateOf(false) }
    val furnitureInputList = remember { mutableStateListOf<FurnitureInput>() }

    val directions = listOf("North", "East", "South", "West")
    val furnitureTypes = listOf("Sofa", "Study Table", "Bed", "Chair", "Lamp")

    AlertDialog(
        modifier = Modifier.fillMaxWidth()
            .border(color = black, width = 5.dp, shape = RoundedCornerShape(corner = CornerSize(24.dp))),
        title = {Text("Add Furniture Objects")},
        containerColor = dialogueBox,
        text = {
            Column(Modifier.fillMaxWidth()) {
                Spacer(Modifier.height(12.dp))

                Text("Select Furniture", fontSize = 18.sp)
                SelectionInputField(furnitureSelection,furnitureTypes, onValueSelection = {selectedValue -> furnitureSelection = selectedValue})

                Spacer(Modifier.height(30.dp))

                Text("Select Directions", fontSize = 18.sp)
                SelectionInputField(directionSelection, directions, onValueSelection = {selectedValue -> directionSelection = selectedValue})

                Spacer(Modifier.height(8.dp))

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Button(modifier = Modifier.height(36.dp).width(100.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = dialogBoxButtonsColor, contentColor = black),
                        onClick = {
                        furnitureInputList.add(FurnitureInput(furnitureType = furnitureSelection, direction = directionSelection))
                        furnitureSelection = "..."
                        directionSelection = "..."
                    }, enabled = furnitureSelection != "..." && directionSelection != "...") {
                        Text("Add More", fontSize = 12.sp)
                    }
                }
            }

        },
        onDismissRequest = {
            cancelOperation()
        },
        dismissButton = {
            CancelButton(onClick = {
                cancelOperation()
            })
        },
        confirmButton = {
            ConfirmButton(onClick = {
                furnitureInputList.add(FurnitureInput(furnitureType = furnitureSelection, direction = directionSelection))
                sendFurnitureObjects(furnitureInputList.toList())
            }, enabled = furnitureSelection != "..." && directionSelection != "...")
        }
    )
}