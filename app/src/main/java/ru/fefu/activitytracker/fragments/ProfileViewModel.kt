package ru.fefu.activitytracker.fragments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.fefu.activitytracker.retrofit.LoginRepository
import ru.fefu.activitytracker.retrofit.Result
import ru.fefu.activitytracker.retrofit.response.UserModel

class ProfileViewModel: ViewModel() {
    private val loginRepository = LoginRepository()

    private val _profile = MutableSharedFlow<Result<UserModel>>(replay = 0)
    private val _logoutUser = MutableSharedFlow<Result<Unit>>(replay = 0)

    val profile get() = _profile
    val logoutUser get() = _logoutUser

    fun getProfile() {
        viewModelScope.launch {
            loginRepository.getProfile()
                .collect {
                    _profile.emit(it)
                }
        }
    }

    fun logout() {
        viewModelScope.launch {
            loginRepository.logout()
                .collect {
                    _logoutUser.emit(it)
                }
        }
    }
}