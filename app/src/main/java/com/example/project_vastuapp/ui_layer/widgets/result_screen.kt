import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project_vastuapp.ui.theme.black
import com.example.project_vastuapp.ui.theme.buttonContainer
import com.example.project_vastuapp.ui.theme.darkGreen
import com.example.project_vastuapp.ui.theme.darkRed
import com.example.project_vastuapp.ui.theme.lightGreen
import com.example.project_vastuapp.ui.theme.lightRed

@SuppressLint("InvalidColorHexValue")
@Composable
fun FurnitureListItemCard(furnitureObject: FurnitureOutput) {
    Card (Modifier.padding(16.dp).height(100.dp), colors = CardDefaults.cardColors(
        containerColor = if(furnitureObject.isCorrect) lightGreen else lightRed
    ), elevation = CardDefaults.cardElevation(2.dp)) {
        Row(modifier =  Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.padding(20.dp).scale(1.5f),
                imageVector = Icons.Filled.Info,
                contentDescription = ""
            )

            Column(Modifier.weight(1f)) {
                Text(furnitureObject.furnitureType, fontSize = 20.sp)
//            Text("Given Directions: ${furnitureObject.directions}")
//            if(!furnitureObject.isDirectionCorrect) {
//                Text("Right Directions: ${furnitureObject.directions}")
//            }
                Spacer(Modifier.height(8.dp))
                Text(furnitureObject.reason, fontSize = 16.sp)
            }
            Icon(
                modifier = Modifier.padding(20.dp).scale(1.5f),
                imageVector = if(furnitureObject.isCorrect) Icons.Filled.Done else Icons.Filled.Close,
                tint = if(furnitureObject.isCorrect) darkGreen else darkRed,
                contentDescription = "Incorrect")
        }
    }

}

@Composable
fun GoToHomePageButton(onClick: () -> Unit) {
    Button(modifier = Modifier.height(60.dp).width(210.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonContainer,
            contentColor = black,
        ),
        onClick = {onClick()}) {
        Text("Go To Home", fontSize = 20.sp)
    }
}