package com.cengizhanerturan.kotlinnewsapplication.data.source.remote

import com.cengizhanerturan.kotlinnewsapplication.core.util.Constants.ERROR_USER_CREDENTIAL
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FirebaseAuthSource @Inject constructor() {
    private val firebaseAuth = FirebaseAuth.getInstance()

    fun getCurrentUser(): FirebaseUser? = firebaseAuth.currentUser

    fun logoutUser(): Boolean {
        try {
            firebaseAuth.signOut()
            return true
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getReloadCurrentUser(): FirebaseUser? {
        val currentUser = firebaseAuth.currentUser ?: throw Exception()
        return try {
            suspendCoroutine { continuation ->
                currentUser.reload().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(currentUser)
                    } else {
                        continuation.resumeWithException(
                            task.exception ?: Exception("Unknown error")
                        )
                    }
                }
            }
        } catch (e: Exception) {
            when (e) {
                is FirebaseAuthInvalidCredentialsException -> {
                    throw Exception(ERROR_USER_CREDENTIAL)
                }

                else -> throw e
            }
        }
    }

    suspend fun registerUser(email: String, password: String): FirebaseUser {
        try {
            return suspendCoroutine { continuation ->
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = firebaseAuth.currentUser ?: throw Exception()
                            continuation.resume(user)
                        } else {
                            continuation.resumeWithException(task.exception ?: Exception())
                        }
                    }
            }
        } catch (e: Exception) {
            throw e
        }
    }


    suspend fun loginUser(email: String, password: String): FirebaseUser {
        try {
            return suspendCoroutine { continuation ->
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = firebaseAuth.currentUser ?: throw Exception()
                            continuation.resume(user)
                        } else {
                            continuation.resumeWithException(task.exception ?: Exception())
                        }
                    }
            }
        } catch (e: Exception) {
            throw e
        }

    }

    suspend fun sendEmailVerification(user: FirebaseUser): Boolean {
        try {
            return suspendCoroutine { continuation ->
                user.sendEmailVerification().addOnCompleteListener { task ->
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


    suspend fun checkUserVerify(user: FirebaseUser): Boolean {
        try {
            return suspendCoroutine { continuation ->
                user.reload().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        if (user.isEmailVerified) {
                            continuation.resume(true)
                        } else {
                            continuation.resume(false)
                        }
                    } else {
                        continuation.resume(false)
                    }
                }
            }
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun updateEmailAddress(
        user: FirebaseUser,
        email: String,
        password: String,
        newEmail: String,
    ): Boolean {
        try {
            return suspendCoroutine { continuation ->
                val credential = EmailAuthProvider.getCredential(email, password)
                user.reauthenticate(credential)
                    .addOnCompleteListener { reAuthTask ->
                        if (reAuthTask.isSuccessful) {
                            user.verifyBeforeUpdateEmail(newEmail).addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    continuation.resume(true)
                                } else {
                                    continuation.resumeWithException(task.exception ?: Exception())
                                }
                            }
                        } else {
                            continuation.resumeWithException(reAuthTask.exception ?: Exception())
                        }
                    }
            }
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun updatePassword(email: String, password: String, newPassword: String): Boolean {
        try {
            return suspendCoroutine { continuation ->
                val user = firebaseAuth.currentUser ?: throw Exception()
                val credential = EmailAuthProvider.getCredential(email, password)
                user.reauthenticate(credential)
                    .addOnCompleteListener { reAuthTask ->
                        if (reAuthTask.isSuccessful) {
                            user.updatePassword(newPassword).addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    continuation.resume(true)
                                } else {
                                    continuation.resumeWithException(task.exception ?: Exception())
                                }
                            }
                        } else {
                            continuation.resumeWithException(reAuthTask.exception ?: Exception())
                        }
                    }
            }
        } catch (e: Exception) {
            throw e
        }
    }
}