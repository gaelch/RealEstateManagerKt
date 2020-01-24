package com.cheyrouse.gael.realestatemanagerkt.controllers.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cheyrouse.gael.realestatemanagerkt.controllers.repositories.AddressDataRepository
import com.cheyrouse.gael.realestatemanagerkt.controllers.repositories.PictureDataRepository
import com.cheyrouse.gael.realestatemanagerkt.controllers.repositories.PropertyDataRepository
import java.util.concurrent.Executor

class ViewModelFactory(
    private val propertyDataRepository: PropertyDataRepository,
    private val addressDataRepository: AddressDataRepository,
    private val pictureDataRepository: PictureDataRepository,
    private val executor: Executor
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PropertyViewModel::class.java)) {
            return PropertyViewModel(
                propertyDataRepository,
                addressDataRepository,
                pictureDataRepository,
                executor
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}