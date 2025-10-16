package com.example.project_vastuapp.ui_layer.state

import android.icu.util.Calendar
import androidx.compose.foundation.layout.size
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlin.random.Random
import kotlin.random.nextInt

enum class TipCategory(val displayName: String, val emoji: String) {
    HOME_DECOR("Home Decor", "🏡"),
    HEALTH("Health", "🌸"),
    WEALTH("Wealth", "💰")
}

data class VastuTip(
    val tip: String,
    val category: TipCategory
)

class VastuTipsViewModel : ViewModel() {

    private val _allTips = MutableStateFlow<List<VastuTip>>(emptyList())

    // State for the selected category chip name
    private val _selectedCategoryName = MutableStateFlow("All")
    val selectedCategoryName: StateFlow<String> = _selectedCategoryName.asStateFlow()

    val categories = listOf("All", "Health", "Wealth", "Home Decor")

    // This derived StateFlow automatically updates when the selected category or the main list changes
    val filteredTips: StateFlow<List<VastuTip>> =
        _selectedCategoryName.combine(_allTips) { categoryName, tips ->
            when (categoryName) {
                "All" -> tips
                "Health" -> tips.filter { it.category == TipCategory.HEALTH }
                "Wealth" -> tips.filter { it.category == TipCategory.WEALTH }
                "Home Decor" -> tips.filter { it.category == TipCategory.HOME_DECOR }
                else -> tips
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        loadVastuTips()
    }

    fun selectCategory(name: String) {
        _selectedCategoryName.value = name
    }

    fun getDailyTip() : String {
        val dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        val year = Calendar.getInstance().get(Calendar.YEAR)
        val seed = dayOfYear + year * 1000
        val random = Random(seed)
        val randomIndex = random.nextInt(_allTips.value.size)
        return _allTips.value[randomIndex].tip
    }

    private fun loadVastuTips() {
        _allTips.value = listOf(
            VastuTip("Avoid having a small room or cabin inside a bedroom — it blocks privacy and energy.", TipCategory.HOME_DECOR),
            VastuTip("Kitchen and toilet should not share a wall.", TipCategory.HOME_DECOR),
            VastuTip("Avoid loud arguments or harsh noises inside the home.", TipCategory.HEALTH),
            VastuTip("Staircases should not begin or end directly at the main door.", TipCategory.HOME_DECOR),
            VastuTip("Avoid keeping trash bins in visible areas.", TipCategory.WEALTH),
            VastuTip("Avoid irregularly shaped rooms — keep them square or rectangular.", TipCategory.HOME_DECOR),
            VastuTip("Use a single mattress for couples instead of two separate ones.", TipCategory.HEALTH),
            VastuTip("Keep your workspace clean and free from unnecessary items.", TipCategory.WEALTH),
            VastuTip("Don’t store heavy items in attics directly above living areas.", TipCategory.HOME_DECOR),
            VastuTip("Use small prosperity symbols like coins, plants, or crystals.", TipCategory.WEALTH),
            VastuTip("Avoid too many mirrors or reflective surfaces — they scatter energy.", TipCategory.HOME_DECOR),
            VastuTip("Keep indoor plants like Tulsi, bamboo, or money plant — they purify and refresh energy.", TipCategory.HEALTH),
            VastuTip("Avoid sleeping or sitting under beams — it creates subconscious pressure.", TipCategory.HEALTH),
            VastuTip("Maintain uniform ceiling heights; avoid slopes within a single room.", TipCategory.HOME_DECOR),
            VastuTip("Declutter rooms regularly — clutter traps stagnant energy.", TipCategory.HEALTH),
            VastuTip("Repair leaking taps — they represent financial loss.", TipCategory.WEALTH),
            VastuTip("Use natural fragrances like sandalwood, lavender, or camphor.", TipCategory.HEALTH),
            VastuTip("Keep family photos in shared spaces to strengthen emotional bonds.", TipCategory.HEALTH),
            VastuTip("Keep your home entrance clean, inviting, and well-lit.", TipCategory.WEALTH),
            VastuTip("Avoid keeping broken or dried plants indoors.", TipCategory.HEALTH),
            VastuTip("Keep fresh flowers or a small decorative plant in the living area.", TipCategory.HEALTH),
            VastuTip("Keep cash, jewelry, and important papers neatly organized.", TipCategory.WEALTH),
            VastuTip("Use soft lighting and soothing wall colors.", TipCategory.HEALTH),
            VastuTip("Ensure all rooms have proper ventilation and natural light.", TipCategory.HOME_DECOR),
            VastuTip("Avoid storing broken items, old wallets, or dead clocks.", TipCategory.WEALTH),
            VastuTip("Avoid clutter near your study or work area.", TipCategory.WEALTH),
            VastuTip("Keep the center area (Brahmasthan) open and free of heavy furniture.", TipCategory.HOME_DECOR),
            VastuTip("Avoid bathrooms above kitchens or pooja rooms.", TipCategory.HOME_DECOR),
            VastuTip("Pay bills and dues on time to maintain positive flow.", TipCategory.WEALTH),
            VastuTip("Keep your surroundings fresh and energetic — prosperity follows cleanliness.", TipCategory.WEALTH)
        )
    }
}