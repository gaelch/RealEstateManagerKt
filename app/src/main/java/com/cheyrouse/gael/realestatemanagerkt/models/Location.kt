package com.cheyrouse.gael.realestatemanagerkt.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Location {

    @SerializedName("lat")
    @Expose
    var lat:Double  = 0.0
    @SerializedName("lng")
    @Expose
    var lng:Double = 0.0


}