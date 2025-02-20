package br.com.sayurienterprise.photostore.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.sayurienterprise.photostore.database.PhotoRepository
import br.com.sayurienterprise.photostore.database.model.Photo
import kotlinx.coroutines.launch

class PhotoViewModel(private val repository: PhotoRepository) : ViewModel() {

    private val TAG: String = PhotoViewModel::class.java.simpleName
    val name = MutableLiveData<String>()
    val age = MutableLiveData<Int>()
    val date = MutableLiveData<String>()
    val photoFileName = MutableLiveData<String>()

    val allPhotos: LiveData<List<Photo>> = repository.allPhotos

    fun insert(photo: Photo) = viewModelScope.launch {
        repository.insert(photo)
    }

    fun logDatabaseContents() {
        viewModelScope.launch {
            val photos = repository.getAllPhotosDirect()
            for (photo in photos) {
                Log.d(
                    TAG,
                    "ID: ${photo.id}, Nome: ${photo.name}, Idade: ${photo.age}, Data: ${photo.date}, Arquivo: ${photo.photoFileName}"
                )
            }
        }
    }
}




