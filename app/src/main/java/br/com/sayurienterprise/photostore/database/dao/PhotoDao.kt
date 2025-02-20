package br.com.sayurienterprise.photostore.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.com.sayurienterprise.photostore.model.Photo

@Dao
public interface PhotoDao {

    @Insert
    fun SavePhoto(photo: Photo?)

    @Query("SELECT * FROM photo_table")
    fun getPhoto(): List<Photo?>?

    @Delete
    fun removePhoto(photo: Photo?)

    @Update
    fun editPhoto(photo : Photo?)
}