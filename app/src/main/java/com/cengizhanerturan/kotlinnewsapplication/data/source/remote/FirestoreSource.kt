package com.cengizhanerturan.kotlinnewsapplication.data.source.remote

import com.cengizhanerturan.kotlinnewsapplication.data.model.UserResponse
import com.cengizhanerturan.kotlinnewsapplication.core.util.Constants.USER_PATH
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FirestoreSource @Inject constructor() {
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun saveUser(userId: String, email: String): Boolean {
        try {
            val userMap = mapOf(
                "userId" to userId,
                "email" to email,
                "created_time" to Timestamp.now()
            )
            return suspendCoroutine { continuation ->
                firestore.collection(USER_PATH).document(userId)
                    .set(userMap)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            continuation.resume(true)
                        } else {
                            continuation.resumeWithException(task.exception ?: Exception())
                        }
                    }
            }
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun updateUserInfo(userId: String, name: String, surname: String): Boolean {
        try {
            val userMap = mapOf(
                "name" to name,
                "surname" to surname
            )
            return suspendCoroutine { continuation ->
                firestore.collection(USER_PATH).document(userId)
                    .update(userMap)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            continuation.resume(true)
                        } else {
                            continuation.resumeWithException(task.exception ?: Exception())
                        }
                    }
            }
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun updateEmailAddress(userId: String, newEmail: String): Boolean {
        try {
            val userMap = mapOf(
                "email" to newEmail,
            )
            return suspendCoroutine { continuation ->
                firestore.collection(USER_PATH).document(userId)
                    .update(userMap)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            continuation.resume(true)
                        } else {
                            continuation.resumeWithException(task.exception ?: Exception())
                        }
                    }
            }
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getUser(userId: String): UserResponse {
        try {
            return suspendCoroutine { continuation ->
                firestore.collection(USER_PATH).document(userId).get()
                    .addOnSuccessListener { document ->
                        if (document.exists()) {
                            val documentData = document.data
                            documentData?.let { data ->
                                val userResponse =
                                    UserResponse(
                                        userId = userId,
                                        name = data.getValue("name") as String,
                                        surname = data.getValue("surname") as String,
                                        email = data.getValue("email") as String
                                    )
                                continuation.resume(userResponse)
                            }

                        } else {
                            continuation.resumeWithException(Exception())
                        }
                    }.addOnFailureListener {
                        continuation.resumeWithException(it)
                    }
            }
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun isEmailRegistered(email: String): Boolean {
        try {
            return suspendCoroutine { continuation ->
                firestore.collection(USER_PATH).whereEqualTo("email", email).get()
                    .addOnSuccessListener { documents ->
                        if (documents.isEmpty) {
                            continuation.resume(false)
                        } else {
                            continuation.resume(true)
                        }
                    }.addOnFailureListener {
                        continuation.resumeWithException(it)
                    }
            }
        } catch (e: Exception) {
            throw e
        }
    }
}