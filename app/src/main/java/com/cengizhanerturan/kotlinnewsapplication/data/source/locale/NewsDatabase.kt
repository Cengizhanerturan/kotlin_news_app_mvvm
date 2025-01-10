package com.cengizhanerturan.kotlinnewsapplication.data.source.locale

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cengizhanerturan.kotlinnewsapplication.data.model.NewsEntity

@Database(entities = [NewsEntity::class], version = 1, exportSchema = false)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun newsDao(): NewsDao
}