package com.cheyrouse.gael.realestatemanagerkt.utils

import com.cheyrouse.gael.realestatemanagerkt.models.GeocodeInfo
import com.cheyrouse.gael.realestatemanagerkt.models.GeocodeLocation
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface RealEstateService {

    @GET("json?")
    fun getGeocodeInfo(
        @Query("address") address: String,
        @Query("key") key: String
    ): Observable<GeocodeInfo>

    companion object {
        fun create(): RealEstateService {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://maps.google.com/maps/api/geocode/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(
                    OkHttpClient.Builder().addInterceptor(
                        HttpLoggingInterceptor().setLevel(
                            HttpLoggingInterceptor.Level.BASIC
                        )
                    ).build()
                )
                .build()

            return retrofit.create(RealEstateService::class.java)
        }
    }
}

