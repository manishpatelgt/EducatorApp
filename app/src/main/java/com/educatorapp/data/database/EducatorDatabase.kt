package com.educatorapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.educatorapp.model.VideoEntity

@Database(entities = [VideoEntity::class], version = 1)
abstract class EducatorDatabase : RoomDatabase() {
    abstract fun getVideoDao(): VideoDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: EducatorDatabase? = null

        fun getDatabase(context: Context): EducatorDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EducatorDatabase::class.java,
                    "educator-database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}