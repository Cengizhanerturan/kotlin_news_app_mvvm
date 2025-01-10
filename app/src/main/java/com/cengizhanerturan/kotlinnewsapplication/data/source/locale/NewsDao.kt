package com.cengizhanerturan.kotlinnewsapplication.data.source.locale

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cengizhanerturan.kotlinnewsapplication.data.model.NewsEntity
import com.cengizhanerturan.kotlinnewsapplication.core.util.Constants.DB_TABLE_NAME

@Dao
interface NewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNews(news: NewsEntity)

    @Query("DELETE FROM $DB_TABLE_NAME WHERE title = :title")
    suspend fun deleteNews(title: String)

    @Query("SELECT * FROM $DB_TABLE_NAME")
    suspend fun getAllSavedNews(): List<NewsEntity>

    @Query("SELECT EXISTS(SELECT * FROM $DB_TABLE_NAME WHERE title = :title LIMIT 1)")
    suspend fun getNewsByTitle(title: String): Boolean
}