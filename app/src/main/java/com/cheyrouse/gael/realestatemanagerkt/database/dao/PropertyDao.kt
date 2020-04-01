package com.cheyrouse.gael.realestatemanagerkt.database.dao

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import androidx.sqlite.db.SupportSQLiteQuery
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

    @RawQuery(observedEntities = [Property::class])
    fun getPropertyByArgs(query: SupportSQLiteQuery) : LiveData<List<Property>>

    //// ---- FOR CONTENT PROVIDER ---- ////

    @Query("SELECT * FROM property WHERE id = :idProperty")
    fun getPropertyWithCursor(idProperty: Long): Cursor

    @Query("SELECT * FROM property")
    fun getAllPropertyWithCursor(): Cursor
}