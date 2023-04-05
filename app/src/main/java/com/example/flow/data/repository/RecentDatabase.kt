package com.example.flow.data.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.flow.data.model.Recent

@Database(entities = [Recent::class], version = 1)
abstract class RecentDatabase : RoomDatabase() {
    abstract fun recentDao() : RecentDao

    companion object {
        private var instance : RecentDatabase? = null

        //데이터베이스 생성 빌더
        @Synchronized
        fun getInstance(context: Context) : RecentDatabase? {
            if (instance == null) {
                synchronized(RecentDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        RecentDatabase::class.java,
                        "Recent_table"
                    ).build()
                }
            }
            return instance
        }
    }
}