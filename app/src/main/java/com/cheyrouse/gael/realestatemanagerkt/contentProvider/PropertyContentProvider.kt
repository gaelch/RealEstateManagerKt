package com.cheyrouse.gael.realestatemanagerkt.contentProvider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.cheyrouse.gael.realestatemanagerkt.database.RealEstateDatabase
import com.cheyrouse.gael.realestatemanagerkt.models.Property


class PropertyContentProvider : ContentProvider() {
    // FOR DATA
    val AUTHORITY = "com.gael.openclassrooms.realestatemanager.provider"
    val TABLE_NAME = Property::class.java.simpleName
    var URI_PROPERTY = Uri.parse("content://$AUTHORITY/$TABLE_NAME")


    override fun insert(p0: Uri, p1: ContentValues?): Uri? {
        throw IllegalArgumentException("You can't insert row into $p0")
    }

    override fun query(
        p0: Uri,
        p1: Array<out String>?,
        p2: String?,
        p3: Array<out String>?,
        p4: String?
    ): Cursor? {
        if (context != null){
            val cursor = RealEstateDatabase.getInstance(context!!).propertyDao().getAllPropertyWithCursor()
            cursor.setNotificationUri(context!!.contentResolver,p0)
            return cursor
        }
        throw IllegalArgumentException("Failed to query row for uri $p0")
    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        throw IllegalArgumentException("You can't update row into $p0")
    }

    override fun delete(p0: Uri, p1: String?, p2: Array<out String>?): Int {
        throw IllegalArgumentException("You can't delete anything")
    }

    override fun getType(p0: Uri): String? {
        return "vnd.android.cursor.item/$AUTHORITY.$TABLE_NAME"
    }

}