package com.cheyrouse.gael.realestatemanagerkt.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "Address", foreignKeys = [ForeignKey(entity = Property::class,
    parentColumns = ["id"],
    childColumns = ["propertyId"])])
data class Address(@PrimaryKey @ColumnInfo(name = "addressId")var addressId:Long,
                   var address:String?,
                   var sector:String?,
                   var apartmentNumber:Int?,
                   var city:String?,
                   var postalCode:String?,
                   var country:String?,
                   var lat:Double?,
                   var lng:Double?,
                   var additionalAddress:String?,
                   @ColumnInfo(name = "propertyId", index = true) var propertyId:Long) :
    Parcelable {
    constructor() : this(0, "", "",0,"","","",0.0,0.0,"",0)

}


