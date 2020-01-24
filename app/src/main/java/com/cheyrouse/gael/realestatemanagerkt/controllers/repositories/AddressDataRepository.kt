package com.cheyrouse.gael.realestatemanagerkt.controllers.repositories

import androidx.lifecycle.LiveData
import com.cheyrouse.gael.realestatemanagerkt.database.dao.AddressDao
import com.cheyrouse.gael.realestatemanagerkt.models.Address

class AddressDataRepository(private val addressDao: AddressDao) {
    // --- GET ---
    fun getAddressListDao(idProperty: Long): LiveData<List<Address>> {
        return addressDao.getAddresses(idProperty)
    }

    fun getAddressDao(idProperty: Long): Address {
        return addressDao.getAddress(idProperty)
    }

    // --- CREATE ---
    fun createAddressDao(address: Address?) {
        addressDao.insertAddress(address!!)
    }

    // --- DELETE ---
    fun deleteAddressDao(id: Long) {
        addressDao.deleteAddress(id)
    }

    // --- UPDATE ---
    fun updateAddressDao(address: Address?) {
        addressDao.updateAddress(address!!)
    }

}
