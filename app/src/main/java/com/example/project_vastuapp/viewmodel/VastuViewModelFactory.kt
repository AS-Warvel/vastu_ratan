package com.example.project_vastuapp.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.project_vastuapp.datalayer.VastuLogic
import com.example.project_vastuapp.datalayer.VastuCalculatorUseCase
import com.example.project_vastuapp.datalayer.VastuRepository
class VastuViewModelFactory(
    private val vastuLogic: VastuLogic,
    private val vastuRepository: VastuRepository,
    private val vastuCalculatorUseCase: VastuCalculatorUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VastuAppViewModel::class.java)) {
            return VastuAppViewModel(vastuLogic, vastuRepository, vastuCalculatorUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}