package com.cheyrouse.gael.realestatemanagerkt.controllers.repositories

import androidx.lifecycle.LiveData
import androidx.sqlite.db.SimpleSQLiteQuery
import com.cheyrouse.gael.realestatemanagerkt.database.dao.PropertyDao
import com.cheyrouse.gael.realestatemanagerkt.models.Property

class PropertyDataRepository(private val propertyDao: PropertyDao) {

    // --- GET ---
    fun getProperty(id: Long): LiveData<Property> {
        return propertyDao.getProperty(id)
    }
    fun getProperties(): LiveData<List<Property>> {
        return propertyDao.getAllProperties()
    }

    fun getPropertyByArgs(query: SimpleSQLiteQuery) : LiveData<List<Property>>{
        return propertyDao.getPropertyByArgs(query)
    }

    // --- CREATE ---
    fun createProperty(property: Property?) {
        propertyDao.insertProperty(property!!)
    }

    // --- UPDATE ---
    fun updateProperty(property: Property?) {
        propertyDao.updateProperty(property!!)
    }
}