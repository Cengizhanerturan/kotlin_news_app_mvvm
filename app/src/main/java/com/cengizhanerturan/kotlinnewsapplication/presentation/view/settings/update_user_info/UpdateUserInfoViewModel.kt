package com.cengizhanerturan.kotlinnewsapplication.presentation.view.settings.update_user_info

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
class UpdateUserInfoViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val userService: UserService,
) : ViewModel() {
    private val _state: MutableLiveData<State> = MutableLiveData(State.Idle)
    val state: LiveData<State>
        get() = _state

    fun getDefaultUser(): User = userService.user!!

    fun clearState() {
        if (_state.value != State.Idle) {
            _state.postValue(State.Idle)
        }
    }

    private fun setDefaultUser(user: User) {
        userService.user = user
    }

    fun updateUserInfo(name: String, surname: String) = viewModelScope.launch {
        try {
            _state.postValue(State.Loading)
            val isUpdated = userRepository.updateUserInfo(
                userId = getDefaultUser().userId,
                name = name,
                surname = surname
            )
            if (isUpdated) {
                val updatedUser = getDefaultUser().copy(name = name, surname = surname)
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