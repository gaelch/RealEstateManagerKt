package com.cheyrouse.gael.realestatemanagerkt.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.cheyrouse.gael.realestatemanagerkt.models.Picture
@Dao
interface PictureDao {

    @Query("SELECT * FROM Picture WHERE id = :propertyId")
    fun getPictureList(propertyId:Long): LiveData<List<Picture>>

    @Query("SELECT * FROM Picture WHERE id = :pictureId")
    fun getPicture(pictureId:Long): Picture

    @Insert
    fun insertPicture(picture: Picture) : Long

    @Update
    fun updatePicture(picture: Picture)

    @Query("DELETE FROM Picture WHERE id = :index")
    fun deletePicture(index: Long)
}