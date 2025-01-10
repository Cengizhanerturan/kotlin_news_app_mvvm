package com.cengizhanerturan.kotlinnewsapplication.presentation.view.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cengizhanerturan.kotlinnewsapplication.domain.model.User
import com.cengizhanerturan.kotlinnewsapplication.domain.repository.UserRepository
import com.cengizhanerturan.kotlinnewsapplication.domain.service.UserService
import com.cengizhanerturan.kotlinnewsapplication.core.util.AuthType
import com.cengizhanerturan.kotlinnewsapplication.core.util.Constants.ERROR_MSG
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val userService: UserService
) : ViewModel() {
    private val _authState: MutableLiveData<AuthState> = MutableLiveData(AuthState.Idle)
    val authState: LiveData<AuthState>
        get() = _authState

    private lateinit var firebaseUser: FirebaseUser

    private lateinit var userId: String
    lateinit var email: String
    lateinit var password: String
    lateinit var name: String
    lateinit var surname: String


    var isLogin: Boolean? = null
    var isUnverifiedEmailAfterUpdate: Boolean = false

    fun emailProcess() = viewModelScope.launch {
        try {
            _authState.postValue(AuthState.Loading)
            val isRegistered = userRepository.isEmailRegistered(email)
            if (isRegistered) {
                _authState.postValue(AuthState.Success(authType = AuthType.LOGIN))
            } else {
                _authState.postValue(AuthState.Success(authType = AuthType.REGISTER))
            }
        } catch (e: Exception) {
            _authState.postValue(AuthState.Error(e.localizedMessage ?: ERROR_MSG))
        }
    }

    fun passwordProcessRegister() = viewModelScope.launch {
        try {
            _authState.postValue(AuthState.Loading)
            val firebaseUser = userRepository.register(email, password)

            this@AuthViewModel.firebaseUser = firebaseUser
            userId = firebaseUser.uid

            val sendMail = userRepository.sendEmailVerification(firebaseUser)
            if (sendMail) {
                val saveUser = userRepository.saveUser(userId, email)
                if (saveUser) {
                    _authState.postValue(
                        AuthState.Success(
                            authType = AuthType.REGISTER,
                            userVerified = false
                        )
                    )
                } else {
                    _authState.postValue(AuthState.Error(ERROR_MSG))
                }
            } else {
                _authState.postValue(AuthState.Error(ERROR_MSG))
            }
        } catch (e: Exception) {
            _authState.postValue(AuthState.Error(e.localizedMessage ?: ERROR_MSG))
        }
    }

    fun passwordProcessLogin() = viewModelScope.launch {
        try {
            _authState.postValue(AuthState.Loading)
            val firebaseUser = userRepository.login(email, password)

            this@AuthViewModel.firebaseUser = firebaseUser
            userId = firebaseUser.uid

            val userVerified = userRepository.checkUserVerify(firebaseUser)
            if (userVerified) {
                userService.user = userRepository.getUser(userId)

                _authState.postValue(
                    AuthState.Success(
                        authType = AuthType.LOGIN,
                        userVerified = true
                    )
                )
            } else {
                val sendMail = userRepository.sendEmailVerification(firebaseUser)
                if (sendMail) {
                    _authState.postValue(
                        AuthState.Success(
                            authType = AuthType.LOGIN,
                            userVerified = false
                        )
                    )
                } else {
                    _authState.postValue(AuthState.Error(ERROR_MSG))
                }
            }
        } catch (e: Exception) {
            _authState.postValue(AuthState.Error(e.localizedMessage ?: ERROR_MSG))
        }
    }

    fun clearState() {
        _authState.postValue(AuthState.Idle)
    }

    fun verifyProcess() = viewModelScope.launch {
        try {
            _authState.postValue(AuthState.Loading)
            while (true) {
                val isVerified = getIsVerified()
                if (isVerified) {
                    val authType = getAuthType()
                    _authState.postValue(
                        AuthState.Success(
                            authType = authType,
                            userVerified = true
                        )
                    )
                    break
                }
                delay(3000)
            }
        } catch (e: Exception) {
            when (e) {
                is FirebaseAuthInvalidUserException -> {
                    _authState.postValue(
                        AuthState.Error(
                            e.localizedMessage ?: ERROR_MSG,
                            loginRequired = true
                        )
                    )
                }

                else -> {
                    _authState.postValue(AuthState.Error(e.localizedMessage ?: ERROR_MSG))
                }
            }
        }
    }

    private suspend fun getIsVerified() = if (isUnverifiedEmailAfterUpdate) {
        userRepository.isEmailVerifiedAfterUpdate()
    } else {
        userRepository.checkUserVerify(firebaseUser)
    }

    private fun getAuthType() = if (isUnverifiedEmailAfterUpdate) {
        AuthType.UNVERIFIED_EMAIL
    } else {
        if (isLogin == true) AuthType.LOGIN else AuthType.REGISTER
    }

    fun registrationDetailsProcess() = viewModelScope.launch {
        try {
            _authState.postValue(AuthState.Loading)
            val isUpdated = userRepository.updateUserInfo(userId, name, surname)
            if (isUpdated) {
                val authType = if (isLogin == true) AuthType.LOGIN else AuthType.REGISTER

                userService.user = User(userId, name, surname, email)

                _authState.postValue(AuthState.Success(authType = authType))
            } else {
                _authState.postValue(AuthState.Error(ERROR_MSG))
            }
        } catch (e: Exception) {
            _authState.postValue(AuthState.Error(e.localizedMessage ?: ERROR_MSG))
        }
    }
}

