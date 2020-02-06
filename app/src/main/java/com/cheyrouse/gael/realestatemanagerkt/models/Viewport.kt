package com.cheyrouse.gael.realestatemanagerkt.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Viewport {

    @SerializedName("northeast")
    @Expose
    var northeastLatLon: Northeast? = null
    @SerializedName("southwest")
    @Expose
    var southwestLatLon: Southwest? = null

}