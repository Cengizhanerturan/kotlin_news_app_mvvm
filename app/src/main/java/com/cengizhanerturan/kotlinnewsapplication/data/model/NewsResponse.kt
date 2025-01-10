package com.cengizhanerturan.kotlinnewsapplication.data.model

import com.cengizhanerturan.kotlinnewsapplication.domain.model.NewsModel
import com.cengizhanerturan.kotlinnewsapplication.core.util.formatDate

data class NewsResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)

fun NewsResponse.toNewsModelList(): List<NewsModel> {
    return articles
        .filter { article ->
            article.title != "[Removed]" && !article.urlToImage.isNullOrEmpty()
        }
        .map { articles ->
            NewsModel(
                title = articles.title ?: "",
                content = articles.content ?: "",
                imageUrl = articles.urlToImage ?: "",
                url = articles.url ?: "",
                date = articles.publishedAt?.formatDate() ?: "",
                sourceName = articles.source?.name ?: "",
            )
        }.toList()
}