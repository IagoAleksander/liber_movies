package com.iaz.libermovies.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.iaz.libermovies.models.MovieModel

@Database(entities = [MovieModel::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun moviesDao(): MoviesDao

    companion object {

        private const val DATABASE_NAME = "movesDb"
        private val LOCK = Any()
        private var sInstance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (sInstance == null) {
                synchronized(LOCK) {
                    sInstance = Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME).build()
                }
            }
            return sInstance!!
        }
    }
}