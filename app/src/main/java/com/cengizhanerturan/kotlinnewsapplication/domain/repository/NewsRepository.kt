package com.cengizhanerturan.kotlinnewsapplication.domain.repository

import androidx.paging.Pager
import com.cengizhanerturan.kotlinnewsapplication.core.util.Constants.NEWS_COUNT
import com.cengizhanerturan.kotlinnewsapplication.data.model.NewsEntity
import com.cengizhanerturan.kotlinnewsapplication.data.model.NewsResponse
import com.cengizhanerturan.kotlinnewsapplication.domain.model.NewsModel

interface NewsRepository {
    suspend fun getTopHeadlines(category: String? = null, pageSize: Int = NEWS_COUNT): NewsResponse

    fun getTopHeadlinesWithPaging(): Pager<Int, NewsModel>

    suspend fun getSearchNews(searchString: String): NewsResponse

    suspend fun saveNews(news: NewsEntity): Boolean

    suspend fun deleteNews(title: String): Boolean

    suspend fun getAllSavedNews(): List<NewsEntity>

    suspend fun isNewsSaved(title: String): Boolean
}