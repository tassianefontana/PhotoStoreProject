package br.com.sayurienterprise.photostore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.sayurienterprise.photostore.database.dao.PhotoDao
import br.com.sayurienterprise.photostore.model.Photo
import kotlinx.coroutines.launch

class PhotoViewModel(private val photoDao: PhotoDao) : ViewModel() {

    val name = MutableLiveData<String>()
    val age = MutableLiveData<Int>()
    val date = MutableLiveData<String>()
    val photoFileName = MutableLiveData<String>()

    val allPhotos: LiveData<List<Photo>> = photoDao.getPhotos()

    fun savePhotoData() {
        val userName = name.value ?: return
        val userAge = age.value ?: return
        val userDate = date.value ?: return
        val fileName = photoFileName.value ?: return

        val photoData = Photo(name = userName, age = userAge, date = userDate, photoFileName = fileName)

        viewModelScope.launch {
            photoDao.insert(photoData)
        }
    }
}

