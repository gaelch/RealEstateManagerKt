package com.cheyrouse.gael.realestatemanagerkt.models

import androidx.room.*


@Entity
data class Property(
    @PrimaryKey(autoGenerate = true) var id: Long,
    var type: String,
    var description: String?,
    var price: String?,
    var livingSpace: Int?,
    var rooms: Int? =0,
    var shops: Boolean?,
    var trainStation: Boolean?,
    var park: Boolean?,
    var airport: Boolean?,
    var subway: Boolean?,
    var school: Boolean?,
    var status: Boolean?,
    var dateOfEntry: String?,
    var dateOfSale: String?,
    var realtor: String?,
    var numOfBath: Int?,
    var numOfBed: Int?,
    @Embedded
    var address: Address?,
    @TypeConverters
    var pictures: List<Picture>? = arrayListOf()
)
{
    constructor() : this(
        0, "", "", "", 0,
        0, false,false,false,false,false,false, true, "", "",
        "",0, 0, null, emptyList()
    )
}







