package com.cengizhanerturan.kotlinnewsapplication.presentation.view.splash

import com.cengizhanerturan.kotlinnewsapplication.core.util.SplashType

sealed class SplashState {
    data class Success(val type: SplashType) : SplashState()
    data object Loading : SplashState()
    data class Error(val message: String) : SplashState()
}