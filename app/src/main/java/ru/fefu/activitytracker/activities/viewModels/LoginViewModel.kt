package ru.fefu.activitytracker.activities.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.fefu.activitytracker.retrofit.LoginRepository
import ru.fefu.activitytracker.retrofit.Result
import ru.fefu.activitytracker.retrofit.response.TokenUserModel

class LoginViewModel:ViewModel() {
    private val loginRepository = LoginRepository()

    private val _dataFlow = MutableSharedFlow<Result<TokenUserModel>>(replay = 0)

    val dataFlow get() = _dataFlow

    fun login(login:String, password:String) {
        viewModelScope.launch {
            loginRepository.login(login, password)
                .collect {
                    _dataFlow.emit(it)
                }
        }
    }
}