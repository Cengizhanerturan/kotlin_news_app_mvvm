package com.cengizhanerturan.kotlinnewsapplication.data.source.remote

import com.cengizhanerturan.kotlinnewsapplication.data.model.NewsResponse
import com.cengizhanerturan.kotlinnewsapplication.core.util.Constants.API_KEY
import com.cengizhanerturan.kotlinnewsapplication.core.util.Constants.DEFAULT_COUNTRY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String = DEFAULT_COUNTRY,
        @Query("pageSize") pageSize: Int? = null,
        @Query("category") category: String? = null,
        @Query("apiKey") apiKey: String = API_KEY
    ): Response<NewsResponse>

    @GET("everything")
    suspend fun getSearchNews(
        @Query("q") searchString: String,
        @Query("apiKey") apiKey: String = API_KEY
    ): Response<NewsResponse>
}