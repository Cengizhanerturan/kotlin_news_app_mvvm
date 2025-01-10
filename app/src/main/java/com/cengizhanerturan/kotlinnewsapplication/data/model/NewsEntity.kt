package com.cengizhanerturan.kotlinnewsapplication.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cengizhanerturan.kotlinnewsapplication.domain.model.NewsModel
import com.cengizhanerturan.kotlinnewsapplication.core.util.Constants.DB_TABLE_NAME


@Entity(tableName = DB_TABLE_NAME)
data class NewsEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val content: String,
    val imageUrl: String,
    val url: String,
    val date: String,
    val sourceName: String
)

fun NewsModel.toNewsEntity(): NewsEntity {
    return NewsEntity(
        title = this.title,
        content = this.content,
        imageUrl = this.imageUrl,
        url = this.url,
        date = this.date,
        sourceName = this.sourceName
    )
}

fun NewsEntity.toNewsModel(): NewsModel {
    return NewsModel(
        title = this.title,
        content = this.content,
        imageUrl = this.imageUrl,
        url = this.url,
        date = this.date,
        sourceName = this.sourceName
    )
}
