package com.cheyrouse.gael.realestatemanagerkt.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.cheyrouse.gael.realestatemanagerkt.models.Property

@Dao
interface PropertyDao {


    @Query("SELECT * from property")
    fun getAllProperties(): LiveData<List<Property>>

    @Query("SELECT * from property WHERE id = :propertyId")
    fun getProperty(propertyId:Long): LiveData<Property>

    @Insert(onConflict = REPLACE)
    fun insertProperty(property: Property)

    @Update
    fun updateProperty(property: Property): Int

    @Query("DELETE FROM property WHERE id = :index")
    fun deleteProperty(index: Long)

}