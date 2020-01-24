package com.cheyrouse.gael.realestatemanagerkt.models

import androidx.room.*


@Entity
//    foreignKeys = arrayOf(
//        ForeignKey(
//            entity = Address::class,
//            parentColumns = arrayOf("id"),
//            childColumns = arrayOf("address")
//        ),
//        ForeignKey(
//            entity = Picture::class,
//            parentColumns = arrayOf("id"),
//            childColumns = arrayOf("pictures")
//        )
//
//    )
//)
data class Property(

    @PrimaryKey(autoGenerate = true) var id: Long = 0,
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
    @Embedded
    var address: Address?,
    @TypeConverters
    var pictures: List<Picture>? = arrayListOf()
)
{
    constructor() : this(
        0, "", "", "", 0,
        0, false,false,false,false,false,false, true, "", "",
        "", null, emptyList()
    )
}







