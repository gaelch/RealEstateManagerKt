package com.cheyrouse.gael.realestatemanagerkt.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(foreignKeys = [ForeignKey(entity = Picture::class,
    parentColumns = ["id"],
    childColumns = ["propertyId"])])
@Parcelize
data class Picture(@PrimaryKey(autoGenerate = true) val id:Long,
                   var pictureName:String?,
                   var picturePath: String,
                   var propertyId:Long
) : Parcelable