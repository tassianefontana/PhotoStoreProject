package br.com.sayurienterprise.photostore.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.com.sayurienterprise.photostore.model.Photo

@Dao
interface PhotoDao {


    @Insert
    fun insert(photoData: Photo)

    @Query("SELECT * FROM photo_table")
    fun getPhotos(): LiveData<List<Photo>>

    @Delete
    fun removePhoto(photo: Photo)

    @Update
    fun editPhoto(photo: Photo)
}

