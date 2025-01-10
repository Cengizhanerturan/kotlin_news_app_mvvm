package com.cengizhanerturan.kotlinnewsapplication.domain.repository

import com.cengizhanerturan.kotlinnewsapplication.domain.model.User
import com.google.firebase.auth.FirebaseUser

interface UserRepository {
    //Auth
    fun getCurrentUser(): FirebaseUser?

    suspend fun login(email: String, password: String): FirebaseUser

    suspend fun register(email: String, password: String): FirebaseUser

    suspend fun sendEmailVerification(user: FirebaseUser): Boolean

    suspend fun checkUserVerify(user: FirebaseUser): Boolean

    suspend fun isEmailRegistered(email: String): Boolean

    suspend fun updatePassword(email: String, password: String, rePassword: String): Boolean

    fun logout(): Boolean

    //Firestore
    suspend fun getUser(userId: String): User

    suspend fun saveUser(userId: String, email: String): Boolean

    suspend fun updateUserInfo(userId: String, name: String, surname: String): Boolean

    //Mixed
    suspend fun updateEmailAddress(email: String, password: String, newEmail: String): Boolean

    suspend fun isEmailVerifiedAfterUpdate(): Boolean
}