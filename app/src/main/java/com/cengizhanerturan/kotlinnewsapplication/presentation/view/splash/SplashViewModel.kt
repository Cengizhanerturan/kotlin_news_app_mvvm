package com.cengizhanerturan.kotlinnewsapplication.presentation.view.splash


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cengizhanerturan.kotlinnewsapplication.domain.repository.UserRepository
import com.cengizhanerturan.kotlinnewsapplication.domain.service.UserService
import com.cengizhanerturan.kotlinnewsapplication.core.util.Constants.ERROR_MSG
import com.cengizhanerturan.kotlinnewsapplication.core.util.SplashType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val userService: UserService,
) : ViewModel() {
    private val _state: MutableLiveData<SplashState> = MutableLiveData(SplashState.Loading)
    val state: LiveData<SplashState>
        get() = _state

    init {
        getState()
    }

    private fun getState() = viewModelScope.launch {
        try {
            val user = userRepository.getCurrentUser()
            if (user == null) {
                _state.postValue(SplashState.Success(type = SplashType.LOGGED_OUT))
            } else {
                userService.user = userRepository.getUser(user.uid)
                val isVerified = userRepository.isEmailVerifiedAfterUpdate()
                if (isVerified) {
                    _state.postValue(SplashState.Success(type = SplashType.LOGGED_IN))
                } else {
                    _state.postValue(SplashState.Success(type = SplashType.UNVERIFIED_EMAIL))
                }
            }
        } catch (e: Exception) {
            _state.postValue(SplashState.Error(e.localizedMessage ?: ERROR_MSG))
        }
    }

}