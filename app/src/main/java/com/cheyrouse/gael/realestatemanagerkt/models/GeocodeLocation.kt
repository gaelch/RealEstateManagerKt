package com.cheyrouse.gael.realestatemanagerkt.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GeocodeLocation {

    @SerializedName("lat")
    @Expose
    var lat: Double? = null
    @SerializedName("lng")
    @Expose
    var lng: Double? = null

}