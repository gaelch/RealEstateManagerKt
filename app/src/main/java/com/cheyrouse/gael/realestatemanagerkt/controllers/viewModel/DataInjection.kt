package com.cheyrouse.gael.realestatemanagerkt.controllers.viewModel

import android.content.Context
import com.cheyrouse.gael.realestatemanagerkt.controllers.repositories.PropertyDataRepository
import com.cheyrouse.gael.realestatemanagerkt.database.RealEstateDatabase
import java.util.concurrent.Executor
import java.util.concurrent.Executors

object DataInjection {

    object Injection {

        private fun providePropertyDataSource(context: Context): PropertyDataRepository {
            val database: RealEstateDatabase = RealEstateDatabase.getInstance(context)
            return PropertyDataRepository(database.propertyDao())
        }

        private fun provideExecutor(): Executor {
            return Executors.newSingleThreadExecutor()
        }

        fun provideViewModelFactory(context: Context): ViewModelFactory {
            val dataSourceItem: PropertyDataRepository = providePropertyDataSource(context)

            val executor: Executor = provideExecutor()
            return ViewModelFactory(dataSourceItem, executor)
        }
    }
}