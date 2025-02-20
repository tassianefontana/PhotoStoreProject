package br.com.sayurienterprise.photostore.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import br.com.sayurienterprise.photostore.constants.DBConfig
import br.com.sayurienterprise.photostore.database.dao.PhotoDao
import br.com.sayurienterprise.photostore.model.Photo

@Database(entities = [Photo::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java, DBConfig.DB_NAME
                        )
                            .build()
                    }
                }
            }
            return INSTANCE
        }
    }

    abstract fun getPhotoDao() : PhotoDao
}