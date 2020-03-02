package com.cheyrouse.gael.realestatemanagerkt.models

import android.net.Uri
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "Picture", foreignKeys = [ForeignKey(entity = Property::class,
    parentColumns = ["id"],
    childColumns = ["propertyId"])])
data class Picture(@PrimaryKey(autoGenerate = true) var id:Long,
                   var pictureName:String?,
                   var picturePath:String?,
                   @ColumnInfo(name = "propertyId", index = true) var propertyId:Long ) :
    Parcelable {constructor() : this(0, "", "", 0)
}





