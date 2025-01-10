package com.cengizhanerturan.kotlinnewsapplication.presentation.view.settings.update_password

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cengizhanerturan.kotlinnewsapplication.domain.model.State
import com.cengizhanerturan.kotlinnewsapplication.domain.repository.UserRepository
import com.cengizhanerturan.kotlinnewsapplication.domain.service.UserService
import com.cengizhanerturan.kotlinnewsapplication.core.util.Constants.ERROR_MSG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdatePasswordViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val userService: UserService
) : ViewModel() {

    private val _state: MutableLiveData<State> = MutableLiveData(State.Idle)
    val state: LiveData<State>
        get() = _state

    fun clearState() {
        if (_state.value != State.Idle) {
            _state.postValue(State.Idle)
        }
    }

    fun updatePassword(password: String, rePassword: String) = viewModelScope.launch {
        try {
            _state.postValue(State.Loading)
            val user = userService.user ?: throw Exception()
            val email = user.email
            val isUpdated = userRepository.updatePassword(email, password, rePassword)
            if (isUpdated) {
                _state.postValue(State.Success)
            } else {
                _state.postValue(State.Error(ERROR_MSG))
            }
        } catch (e: Exception) {
            _state.postValue(State.Error(e.localizedMessage ?: ERROR_MSG))
        }
    }
}