package com.cengizhanerturan.kotlinnewsapplication.domain.model

sealed class Resource<T> {
    data class Success<T>(var data: T) : Resource<T>()
    data class Error<T>(val message: String) : Resource<T>()
    class Loading<T> : Resource<T>()
}
