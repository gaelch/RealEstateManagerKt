package com.cheyrouse.gael.realestatemanagerkt.models

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.sql.Date


@Entity(
    foreignKeys = arrayOf(
        ForeignKey(
            entity = TypeOfProperty::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("property_type")
        ),
        ForeignKey(
            entity = Address::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("id_address")
        ),
        ForeignKey(
            entity = Realtor::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("id_realtor")
        )
//        ForeignKey(
//            entity = Realtor::class,
//            parentColumns = arrayOf("id"),
//            childColumns = arrayOf("id_realtor")
//        )

    )
)
@Parcelize
data class Property (

    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "property_type") val type: String?,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "price") val price: String?,
    @ColumnInfo(name = "living_space") val livingSpace: Int?,
    @ColumnInfo(name = "number_of_rooms") val rooms: Int?,
    @ColumnInfo(name = "id_address") val idAdresse: Int?,
    @ColumnInfo(name = "points_of_interest") val pointsOfInterest: Int?,
    @ColumnInfo(name = "status") val status: Boolean?,
    @ColumnInfo(name = "date_of_entry") val dateOfEntry: Date?,
    @ColumnInfo(name = "date_of_sale") val dateOfSale: Date?,
    @ColumnInfo(name = "id_realtor") val realtor: String?,
    @ColumnInfo(name = "pictures") val pictures: List<Picture>?
) : Parcelable



