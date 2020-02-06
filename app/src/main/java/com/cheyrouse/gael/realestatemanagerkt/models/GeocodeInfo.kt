package com.cheyrouse.gael.realestatemanagerkt.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GeocodeInfo {

    @SerializedName("results")
    @Expose
    var results: List<Result>? = null
    @SerializedName("status")
    @Expose
    var status: String? = null

}
