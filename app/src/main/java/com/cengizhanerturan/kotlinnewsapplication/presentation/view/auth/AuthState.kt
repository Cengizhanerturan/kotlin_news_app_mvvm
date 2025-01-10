package com.cengizhanerturan.kotlinnewsapplication.presentation.view.auth

import com.cengizhanerturan.kotlinnewsapplication.core.util.AuthType

sealed class AuthState {
    data class Success(val authType: AuthType, val userVerified: Boolean? = null) : AuthState()
    data class Error(val message: String, val loginRequired: Boolean? = null) : AuthState()
    data object Loading : AuthState()
    data object Idle : AuthState()
}