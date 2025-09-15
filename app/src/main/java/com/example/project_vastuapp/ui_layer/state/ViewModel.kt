import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class VastuAppViewModel : ViewModel() {
    private val _currentPage = MutableStateFlow("Home")
    val currentPage: StateFlow<String> = _currentPage

    private val _furnitureInputObjects = MutableStateFlow(listOf<FurnitureInput>())
    val furnitureInputObjects: StateFlow<List<FurnitureInput>> = _furnitureInputObjects

    fun navigateTo(page: String) {
        _currentPage.value = page
    }

    fun setFurnitureInputObjects(listObjects: List<FurnitureInput>) {
        _furnitureInputObjects.value = listObjects
    }

    fun getFurnitureOutputList() : List<FurnitureOutput> {
        return VastuLogic().evaluateFurnitureInputList(_furnitureInputObjects.value)
    }
}