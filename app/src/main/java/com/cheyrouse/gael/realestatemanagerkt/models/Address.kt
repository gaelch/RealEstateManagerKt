package com.cheyrouse.gael.realestatemanagerkt.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Address (
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "property_type") val type: String?
    )