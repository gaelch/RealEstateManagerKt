package com.cheyrouse.gael.realestatemanagerkt.utils

import androidx.room.TypeConverter
import com.cheyrouse.gael.realestatemanagerkt.models.Picture
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*


class Converters {

    var gson = Gson()

    @TypeConverter
    fun stringToSomeObjectList(data: String?): List<Picture?>? {
        if (data == null) {
            return Collections.emptyList()
        }
        val listType: Type =
            object : TypeToken<List<Picture?>?>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun someObjectListToString(someObjects: List<Picture?>?): String? {
        return gson.toJson(someObjects)
    }
}