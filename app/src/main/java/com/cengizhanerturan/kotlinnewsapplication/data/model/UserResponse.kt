package com.cengizhanerturan.kotlinnewsapplication.data.model

import com.cengizhanerturan.kotlinnewsapplication.domain.model.User

data class UserResponse(
    val userId: String,
    val name: String,
    val surname: String,
    val email: String,
)

fun UserResponse.toDomainModel(): User {
    return User(userId, name, surname, email)
}
