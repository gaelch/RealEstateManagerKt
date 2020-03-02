package com.cheyrouse.gael.realestatemanagerkt.utils

import android.R.attr
import androidx.room.TypeConverter
import com.cheyrouse.gael.realestatemanagerkt.models.Address
import com.cheyrouse.gael.realestatemanagerkt.models.Picture
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import java.lang.reflect.Type
import java.util.*


class Converters {
//    @TypeConverter
//    fun restoreList(listOfString: String?): List<String?>? {
//        return Gson().fromJson(
//            listOfString,
//            object : TypeToken<List<String?>?>() {}.getType()
//        )
//    }
//
//    @TypeConverter
//    fun saveList(listOfString: List<String?>?): String? {
//        return Gson().toJson(listOfString)
//    }

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