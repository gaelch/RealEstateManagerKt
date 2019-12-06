package com.cheyrouse.gael.realestatemanagerkt.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Realtor (
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "name") val type: String?
)