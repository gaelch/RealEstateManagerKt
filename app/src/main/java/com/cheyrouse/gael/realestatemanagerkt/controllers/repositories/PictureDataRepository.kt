package com.cheyrouse.gael.realestatemanagerkt.controllers.repositories

import androidx.lifecycle.LiveData
import com.cheyrouse.gael.realestatemanagerkt.database.dao.PictureDao
import com.cheyrouse.gael.realestatemanagerkt.models.Picture

class PictureDataRepository(private val pictureDao: PictureDao) {
    // --- GET ---
    fun getPictureListDao(idProperty: Long): LiveData<List<Picture>> {
        return pictureDao.getPictureList(idProperty)
    }

    fun getPictureDao(idPicture: Long): Picture {
        return pictureDao.getPicture(idPicture)
    }

    // --- CREATE ---
    fun createPictureDao(picture: Picture?) {
        pictureDao.insertPicture(picture!!)
    }

    // --- DELETE ---
    fun deletePictureDao(path: String) {
        pictureDao.deletePicture(path)
    }

    // --- UPDATE ---
    fun updatePictureDao(picture: Picture?) {
        pictureDao.updatePicture(picture!!)
    }

}