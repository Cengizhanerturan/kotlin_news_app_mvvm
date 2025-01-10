package com.cengizhanerturan.kotlinnewsapplication.data.repository

import com.cengizhanerturan.kotlinnewsapplication.data.model.toDomainModel
import com.cengizhanerturan.kotlinnewsapplication.data.source.remote.FirebaseAuthSource
import com.cengizhanerturan.kotlinnewsapplication.data.source.remote.FirestoreSource
import com.cengizhanerturan.kotlinnewsapplication.domain.model.User
import com.cengizhanerturan.kotlinnewsapplication.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firebaseAuthSource: FirebaseAuthSource,
    private val firestoreSource: FirestoreSource
) : UserRepository {
    override fun getCurrentUser(): FirebaseUser? {
        try {
            return firebaseAuthSource.getCurrentUser()
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun login(email: String, password: String): FirebaseUser {
        try {
            return firebaseAuthSource.loginUser(email, password)
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun register(email: String, password: String): FirebaseUser {
        try {
            return firebaseAuthSource.registerUser(email, password)
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun sendEmailVerification(user: FirebaseUser): Boolean {
        try {
            return firebaseAuthSource.sendEmailVerification(user)
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun checkUserVerify(user: FirebaseUser): Boolean {
        try {
            return firebaseAuthSource.checkUserVerify(user)
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun isEmailRegistered(email: String): Boolean {
        try {
            return firestoreSource.isEmailRegistered(email)
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun updatePassword(
        email: String,
        password: String,
        rePassword: String
    ): Boolean {
        try {
            return firebaseAuthSource.updatePassword(email, password, rePassword)
        } catch (e: Exception) {
            throw e
        }
    }

    override fun logout(): Boolean {
        try {
            return firebaseAuthSource.logoutUser()
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getUser(userId: String): User {
        try {
            val userResponse = firestoreSource.getUser(userId)
            return userResponse.toDomainModel()
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun saveUser(userId: String, email: String): Boolean {
        try {
            return firestoreSource.saveUser(userId, email)
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun updateUserInfo(userId: String, name: String, surname: String): Boolean {
        try {
            return firestoreSource.updateUserInfo(userId, name, surname)
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun updateEmailAddress(
        email: String,
        password: String,
        newEmail: String
    ): Boolean {
        try {
            val currentUser = firebaseAuthSource.getCurrentUser()
            if (currentUser != null) {
                val isAuthUpdated =
                    firebaseAuthSource.updateEmailAddress(
                        user = currentUser,
                        email = email,
                        password = password,
                        newEmail = newEmail
                    )
                if (isAuthUpdated) {
                    val isFirestoreUpdated = firestoreSource.updateEmailAddress(
                        userId = currentUser.uid,
                        newEmail = newEmail
                    )
                    if (isFirestoreUpdated) {
                        return true
                    }
                }
                return false
            } else {
                throw Exception()
            }

        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun isEmailVerifiedAfterUpdate(): Boolean {
        try {
            val currentUser = firebaseAuthSource.getReloadCurrentUser()
            if (currentUser != null) {
                val authEmail = currentUser.email
                val firestoreEmail = firestoreSource.getUser(currentUser.uid).email
                return authEmail == firestoreEmail
            } else {
                throw Exception()
            }

        } catch (e: Exception) {
            throw e
        }
    }
}