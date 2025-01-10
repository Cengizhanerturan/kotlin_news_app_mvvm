package com.cengizhanerturan.kotlinnewsapplication.domain.repository

import com.cengizhanerturan.kotlinnewsapplication.data.model.NewsEntity
import com.cengizhanerturan.kotlinnewsapplication.data.model.NewsResponse

interface NewsRepository {
    suspend fun getTopHeadlines(category: String? = null, pageSize: Int? = null): NewsResponse

    suspend fun getSearchNews(searchString: String): NewsResponse

    suspend fun saveNews(news: NewsEntity): Boolean

    suspend fun deleteNews(title: String): Boolean

    suspend fun getAllSavedNews(): List<NewsEntity>

    suspend fun isNewsSaved(title: String): Boolean
}