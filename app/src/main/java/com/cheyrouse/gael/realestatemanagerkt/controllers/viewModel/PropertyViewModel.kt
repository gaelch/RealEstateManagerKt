package com.cheyrouse.gael.realestatemanagerkt.controllers.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.cheyrouse.gael.realestatemanagerkt.controllers.repositories.AddressDataRepository
import com.cheyrouse.gael.realestatemanagerkt.controllers.repositories.PictureDataRepository
import com.cheyrouse.gael.realestatemanagerkt.controllers.repositories.PropertyDataRepository
import com.cheyrouse.gael.realestatemanagerkt.models.Address
import com.cheyrouse.gael.realestatemanagerkt.models.Picture
import com.cheyrouse.gael.realestatemanagerkt.models.Property
import java.util.concurrent.Executor


class PropertyViewModel(private val mPropertyDataRepository: PropertyDataRepository,
                        private val mAddressDataRepository: AddressDataRepository,
                        private val mPictureDataRepository: PictureDataRepository,
                        private val executor: Executor): ViewModel() {

    ///// --- FOR PROPERTY --- /////

    // --- GET ---
    fun getAllProperty(): LiveData<List<Property>> {
        return mPropertyDataRepository.getProperties()
    }
    fun getProperty(propertyId: Long): LiveData<Property> {
        return mPropertyDataRepository.getProperty(propertyId)
    }
    fun createProperty(property: Property?) {
        executor.execute { mPropertyDataRepository.createProperty(property) }
    }

    fun deleteProperty(propertyId: Long) {
        executor.execute { mPropertyDataRepository.deleteProperty(propertyId) }
    }

    fun updateProperty(property: Property?) {
        executor.execute { mPropertyDataRepository.updateProperty(property) }
    }

    ///// --- FOR PICTURE --- /////

    // --- GET ---
    fun getAllPictures(propertyId: Long): LiveData<List<Picture>> {
        return mPictureDataRepository.getPictureListDao(propertyId)
    }

    // --- CREATE ---
    fun createPictureDao(picture: Picture?) {
        mPictureDataRepository.createPictureDao(picture!!)
    }

    // --- DELETE ---
    fun deletePictureDao(id: Long) {
        mPictureDataRepository.deletePictureDao(id)
    }

    // --- UPDATE ---
    fun updatePictureDao(picture: Picture?) {
        mPictureDataRepository.updatePictureDao(picture!!)
    }


    ///// --- FOR ADDRESS --- /////

    // --- GET ---
    fun getAddressListDao(idProperty: Long): LiveData<List<Address>> {
        return mAddressDataRepository.getAddressListDao(idProperty)
    }
    fun getAddressDao(idProperty: Long): Address {
        return mAddressDataRepository.getAddressDao(idProperty)
    }

    // --- CREATE ---
    fun createAddressDao(address: Address?) {
        mAddressDataRepository.createAddressDao(address!!)
    }

    // --- DELETE ---
    fun deleteAddress(id: Long) {
        mAddressDataRepository.deleteAddressDao(id)
    }

    // --- UPDATE ---
    fun updateAddressDao(address: Address?) {
        mAddressDataRepository.updateAddressDao(address!!)
    }

}