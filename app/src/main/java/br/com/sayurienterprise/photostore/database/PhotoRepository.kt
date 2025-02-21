package br.com.sayurienterprise.photostore.database

import androidx.lifecycle.LiveData
import br.com.sayurienterprise.photostore.database.dao.PhotoDao
import br.com.sayurienterprise.photostore.database.model.Photo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PhotoRepository(private val photoDao: PhotoDao) {

    val allPhotos: LiveData<List<Photo>> = photoDao.getPhotos()

    suspend fun insert(photo: Photo) {
        withContext(Dispatchers.IO) {  // Garante que o DAO seja chamado na Thread correta
            photoDao.insert(photo)
        }
    }

    suspend fun getAllPhotosDirect(): List<Photo> {
        return photoDao.getAllPhotos()
    }
}