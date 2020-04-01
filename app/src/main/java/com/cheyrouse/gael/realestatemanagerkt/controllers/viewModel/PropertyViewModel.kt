package com.cheyrouse.gael.realestatemanagerkt.controllers.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.sqlite.db.SimpleSQLiteQuery
import com.cheyrouse.gael.realestatemanagerkt.controllers.repositories.PropertyDataRepository
import com.cheyrouse.gael.realestatemanagerkt.models.Property
import java.util.concurrent.Executor


class PropertyViewModel(private val mPropertyDataRepository: PropertyDataRepository,
                        private val executor: Executor): ViewModel() {

    ///// --- PROPERTY --- /////

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

    fun updateProperty(property: Property?) {
        executor.execute { mPropertyDataRepository.updateProperty(property) }
    }

    //// --- SEARCH --- ////

    fun getPropertyByArgs(queryString:String) : LiveData<List<Property>>{
        val query = SimpleSQLiteQuery(queryString)
        Log.e("get properties by args","Query : ${query.sql}")
        return mPropertyDataRepository.getPropertyByArgs(query)
    }
}