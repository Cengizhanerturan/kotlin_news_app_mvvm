package com.cengizhanerturan.kotlinnewsapplication.domain.model

sealed class State {
    data object Success : State()
    data object Loading : State()
    data class Error(val message: String) : State()
    data object Idle : State()
}