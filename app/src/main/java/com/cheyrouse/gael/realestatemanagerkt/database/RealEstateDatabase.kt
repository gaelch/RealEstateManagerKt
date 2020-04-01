package com.cheyrouse.gael.realestatemanagerkt.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.cheyrouse.gael.realestatemanagerkt.database.dao.PropertyDao
import com.cheyrouse.gael.realestatemanagerkt.models.Address
import com.cheyrouse.gael.realestatemanagerkt.models.Picture
import com.cheyrouse.gael.realestatemanagerkt.models.Property
import com.cheyrouse.gael.realestatemanagerkt.utils.Converters

@Database(entities = [(Property::class), (Picture::class), (Address::class)],version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class RealEstateDatabase : RoomDatabase() {

    abstract fun propertyDao(): PropertyDao

    companion object {
        private var INSTANCE: RealEstateDatabase? = null

        fun getInstance(context: Context):RealEstateDatabase{
            if (INSTANCE == null){
                synchronized(this){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,RealEstateDatabase::class.java,"RealEstateManager.db").build()
                }
            }
            return INSTANCE as RealEstateDatabase
        }
    }
}