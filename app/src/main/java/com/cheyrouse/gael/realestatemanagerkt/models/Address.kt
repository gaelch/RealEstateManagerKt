package com.cheyrouse.gael.realestatemanagerkt.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = [ForeignKey(entity = Address::class,
    parentColumns = ["addressId"],
    childColumns = ["propertyId"])])
data class Address(@PrimaryKey var addressId:Long,
                   var address:String?,
                   var sector:String?,
                   var apartmentNumber:Int?,
                   var city:String?,
                   var postalCode:String?,
                   var country:String?,
                   var additionalAddress:String?,
                   @ColumnInfo(name = "propertyId", index = true) var propertyId:Long)
{
    constructor() : this(0, "", "",0,"","","","",0)

}


