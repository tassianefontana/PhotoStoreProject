package br.com.sayurienterprise.photostore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.sayurienterprise.photostore.database.dao.PhotoDao


class PhotoViewModelFactory(private val repository:PhotoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PhotoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PhotoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
