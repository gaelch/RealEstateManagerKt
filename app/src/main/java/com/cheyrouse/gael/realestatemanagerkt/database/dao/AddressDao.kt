package com.cheyrouse.gael.realestatemanagerkt.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.cheyrouse.gael.realestatemanagerkt.models.Address

@Dao
interface AddressDao {

    @Query("SELECT * FROM Address WHERE addressId = :index")
    fun getAddresses(index:Long): LiveData<List<Address>>

    @Query("SELECT * FROM Address WHERE addressId = :index")
    fun getAddress(index:Long): Address

    @Query("SELECT DISTINCT sector FROM Address WHERE NOT sector = '' ")
    fun getSectorList() : List<String>

    @Insert
    fun insertAddress(address: Address) : Long

    @Query("DELETE FROM address WHERE addressId = :index")
    fun deleteAddress(index: Long)

    @Update
    fun updateAddress(address: Address)
}