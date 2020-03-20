package com.cheyrouse.gael.realestatemanagerkt.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "Picture")
data class Picture(@PrimaryKey(autoGenerate = true) var id:Long,
                   var pictureName:String?,
                   var picturePath:String?) :
    Parcelable {constructor() : this(0, "", "")
}





