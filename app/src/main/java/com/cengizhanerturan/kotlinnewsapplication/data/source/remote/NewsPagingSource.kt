package com.cengizhanerturan.kotlinnewsapplication.data.source.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.cengizhanerturan.kotlinnewsapplication.data.model.toNewsModelList
import com.cengizhanerturan.kotlinnewsapplication.domain.model.NewsModel

class NewsPagingSource(
    private val apiService: ApiService
) : PagingSource<Int, NewsModel>() {
    override fun getRefreshKey(state: PagingState<Int, NewsModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NewsModel> {
        val page = params.key ?: 1
        return try {
            val response = apiService.getTopHeadlines(pageSize = params.loadSize, page = page)
            var data = if (response.isSuccessful) {
                response.body()?.toNewsModelList()
                    ?: throw Exception()
            } else {
                throw Exception()
            }

            if (page == 1) data = data.drop(5)

            LoadResult.Page(
                data = data,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (data.isEmpty()) null else page + 1,
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}