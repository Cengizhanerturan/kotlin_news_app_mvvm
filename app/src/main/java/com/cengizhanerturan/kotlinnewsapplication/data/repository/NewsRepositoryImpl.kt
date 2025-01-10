package com.cengizhanerturan.kotlinnewsapplication.data.repository

import com.cengizhanerturan.kotlinnewsapplication.data.model.NewsEntity
import com.cengizhanerturan.kotlinnewsapplication.data.model.NewsResponse
import com.cengizhanerturan.kotlinnewsapplication.data.source.locale.NewsDao
import com.cengizhanerturan.kotlinnewsapplication.data.source.remote.ApiService
import com.cengizhanerturan.kotlinnewsapplication.domain.repository.NewsRepository
import com.cengizhanerturan.kotlinnewsapplication.core.util.Constants.ERROR_MSG
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val newsDao: NewsDao
) : NewsRepository {
    override suspend fun getTopHeadlines(category: String?, pageSize: Int?): NewsResponse {
        try {
            val response = apiService.getTopHeadlines(category = category, pageSize = pageSize)
            if (response.isSuccessful) {
                response.body()?.let { newsResponseList ->
                    return newsResponseList
                } ?: throw Exception()
            } else {
                throw Exception()
            }
        } catch (e: Exception) {
            throw Exception(e.localizedMessage ?: ERROR_MSG)
        }
    }

    override suspend fun getSearchNews(searchString: String): NewsResponse {
        try {
            val response = apiService.getSearchNews(searchString)
            if (response.isSuccessful) {
                response.body()?.let { newsResponse ->
                    return newsResponse
                } ?: throw Exception()
            } else {
                throw Exception()
            }
        } catch (e: Exception) {
            throw Exception(e.localizedMessage ?: ERROR_MSG)
        }
    }

    override suspend fun saveNews(news: NewsEntity): Boolean {
        try {
            newsDao.insertNews(news = news)
            return true
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun deleteNews(title: String): Boolean {
        try {
            newsDao.deleteNews(title)
            return true
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getAllSavedNews(): List<NewsEntity> {
        try {
            return newsDao.getAllSavedNews()
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun isNewsSaved(title: String): Boolean {
        try {
            return newsDao.getNewsByTitle(title)
        } catch (e: Exception) {
            throw e
        }
    }
}