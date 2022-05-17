package com.iraimjanov.smallchat.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.iraimjanov.smallchat.cc.CountryDB

@Database(entities = [CountryDB::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun myDao(): MyDao

    companion object {
        private var instance: AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(context, AppDatabase::class.java, "dictionary")
                    .fallbackToDestructiveMigration().allowMainThreadQueries().build()
            }
            return instance!!
        }
    }
}