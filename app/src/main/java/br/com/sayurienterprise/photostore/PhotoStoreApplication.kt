package br.com.sayurienterprise.photostore

import android.app.Application
import br.com.sayurienterprise.photostore.database.AppDatabase

class PhotoStoreApplication : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
}