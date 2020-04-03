package com.cheyrouse.gael.realestatemanagerkt.utils

import com.cheyrouse.gael.realestatemanagerkt.models.GeocodeInfo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class RealEstateStream {

    // Stream function
    fun streamFetchGeocodeInfo(address: String, key: String): Observable<GeocodeInfo> =
        RealEstateService.create().getGeocodeInfo(address, key)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .timeout(10, TimeUnit.SECONDS)
}