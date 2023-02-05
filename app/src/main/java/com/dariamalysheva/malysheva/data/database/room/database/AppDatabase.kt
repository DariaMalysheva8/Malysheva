package com.dariamalysheva.malysheva.data.database.room.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dariamalysheva.malysheva.data.database.room.dao.FilmsDao
import com.dariamalysheva.malysheva.data.database.room.entities.FavoriteFilmsDB
import com.dariamalysheva.malysheva.data.database.room.entities.PopularFilmsDB

@Database(
    entities = [PopularFilmsDB::class, FavoriteFilmsDB::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getFilmsDao(): FilmsDao

    companion object {

        private const val DATABASE_NAME = "films_database"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(application: Application): AppDatabase {

            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    application,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                    .build()
                INSTANCE = instance

                instance
            }
        }
    }
}