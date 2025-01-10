package com.cengizhanerturan.kotlinnewsapplication.presentation.view.settings.update_email_address

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cengizhanerturan.kotlinnewsapplication.domain.model.State
import com.cengizhanerturan.kotlinnewsapplication.domain.model.User
import com.cengizhanerturan.kotlinnewsapplication.domain.repository.UserRepository
import com.cengizhanerturan.kotlinnewsapplication.domain.service.UserService
import com.cengizhanerturan.kotlinnewsapplication.core.util.Constants.ERROR_MSG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateEmailAddressViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val userService: UserService
) : ViewModel() {
    private val _state: MutableLiveData<State> = MutableLiveData(State.Idle)
    val state: LiveData<State>
        get() = _state

    fun getDefaultUser(): User = userService.user!!

    private fun setDefaultUser(user: User) {
        userService.user = user
    }

    fun clearState() {
        if (_state.value != State.Idle) {
            _state.postValue(State.Idle)
        }
    }

    fun updateEmailAddress(password: String, newEmail: String) = viewModelScope.launch {
        try {
            _state.postValue(State.Loading)
            val isUpdated = userRepository.updateEmailAddress(
                email = getDefaultUser().email,
                password = password,
                newEmail = newEmail
            )
            if (isUpdated) {
                val updatedUser = getDefaultUser().copy(
                    email = newEmail
                )
                setDefaultUser(updatedUser)
                _state.postValue(State.Success)
            } else {
                _state.postValue(State.Error(message = ERROR_MSG))
            }
        } catch (e: Exception) {
            _state.postValue(State.Error(message = e.localizedMessage ?: ERROR_MSG))
        }
    }
}