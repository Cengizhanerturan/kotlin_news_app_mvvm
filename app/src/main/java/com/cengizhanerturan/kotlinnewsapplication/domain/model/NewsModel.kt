package com.cengizhanerturan.kotlinnewsapplication.domain.model

import java.io.Serializable

data class NewsModel(
    val title: String,
    val content: String,
    val imageUrl: String,
    val url: String,
    val date: String,
    val sourceName: String,
) : Serializable
