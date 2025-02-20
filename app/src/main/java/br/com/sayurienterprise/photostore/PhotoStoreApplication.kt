package br.com.sayurienterprise.photostore

import android.app.Application
import android.util.Log
import br.com.sayurienterprise.photostore.database.AppDatabase
import br.com.sayurienterprise.photostore.database.PhotoRepository
import br.com.sayurienterprise.photostore.helper.CameraHelper
import br.com.sayurienterprise.photostore.helper.PermissionsHelper

class PhotoStoreApplication : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
    val repository: PhotoRepository by lazy { PhotoRepository(database.photoDao()) }
    val cameraHelper : CameraHelper by lazy { CameraHelper(this) }
    val permissionsHelper: PermissionsHelper by lazy { PermissionsHelper(this) }

    override fun onCreate() {
        super.onCreate()
        val db = AppDatabase.getDatabase(this)
        Log.d("DATABASE_INIT", "Init database: ${db.openHelper.databaseName}")
    }

}