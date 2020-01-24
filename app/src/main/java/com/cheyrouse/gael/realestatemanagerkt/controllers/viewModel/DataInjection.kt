package com.cheyrouse.gael.realestatemanagerkt.controllers.viewModel

import android.content.Context
import com.cheyrouse.gael.realestatemanagerkt.controllers.repositories.AddressDataRepository
import com.cheyrouse.gael.realestatemanagerkt.controllers.repositories.PictureDataRepository
import com.cheyrouse.gael.realestatemanagerkt.controllers.repositories.PropertyDataRepository
import com.cheyrouse.gael.realestatemanagerkt.database.RealEstateDatabase
import java.util.concurrent.Executor
import java.util.concurrent.Executors

object DataInjection {

    object Injection {

        fun providePropertyDataSource(context: Context): PropertyDataRepository {
            val database: RealEstateDatabase = RealEstateDatabase.getInstance(context)
            return PropertyDataRepository(database.propertyDao())
        }

        fun provideAddressDataSource(context: Context): AddressDataRepository {
            val database: RealEstateDatabase = RealEstateDatabase.getInstance(context)
            return AddressDataRepository(database.addressDao())
        }

        fun providePictureDataSource(context: Context): PictureDataRepository {
            val database: RealEstateDatabase = RealEstateDatabase.getInstance(context)
            return PictureDataRepository(database.pictureDao())
        }

        fun provideExecutor(): Executor {
            return Executors.newSingleThreadExecutor()
        }

        fun provideViewModelFactory(context: Context): ViewModelFactory {
            val dataSourceItem: PropertyDataRepository = providePropertyDataSource(context)
            val dataSourceAddress: AddressDataRepository = provideAddressDataSource(context)
            val dataSourceUser: PictureDataRepository = providePictureDataSource(context)
            val executor: Executor = provideExecutor()
            return ViewModelFactory(dataSourceItem, dataSourceAddress, dataSourceUser, executor)
        }
    }
}