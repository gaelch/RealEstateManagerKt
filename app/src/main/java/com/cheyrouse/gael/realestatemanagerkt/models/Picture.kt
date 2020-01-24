package com.cheyrouse.gael.realestatemanagerkt.models

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(foreignKeys = [ForeignKey(entity = Picture::class,
    parentColumns = ["id"],
    childColumns = ["propertyId"])])
data class Picture(@PrimaryKey(autoGenerate = true) var id:Long,
                   var pictureName:String?,
                   var picturePath:String?,
                   @ColumnInfo(name = "propertyId", index = true) var propertyId:Long )
{constructor() : this(0, "", "", 0)
}





