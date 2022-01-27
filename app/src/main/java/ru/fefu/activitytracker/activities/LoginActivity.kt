package ru.fefu.activitytracker.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.serialization.*
import ru.fefu.activitytracker.App
import ru.fefu.activitytracker.R
import ru.fefu.activitytracker.activities.viewModels.LoginViewModel
import ru.fefu.activitytracker.retrofit.Result
import ru.fefu.activitytracker.retrofit.response.TokenUserModel
class LoginActivity : AppCompatActivity() {
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        setContentView(R.layout.login)

        val backButton  = findViewById<MaterialButton>(R.id.login_button_back)
        backButton.setOnClickListener {
            val intent = Intent(this@LoginActivity, WelcomeActivity::class.java)
            startActivity(intent)
        }

        viewModel.dataFlow
            .onEach {
                if (it is Result.Success<TokenUserModel>) {
                    App.INSTANCE.sharedPrefs.edit().putString("token", it.result.token).apply()
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
                else if (it is Result.Error<TokenUserModel>) {
                    //Toast.makeText(this, it.e.toString(), Toast.LENGTH_LONG).show()
                    var error = it.e
                    if (error is retrofit2.HttpException) {
                        if (error.response()?.code() == 422){
                                Toast.makeText(this,
                                    "Неверный логин или пароль!",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                    }else{

                }
            }else{
                }
            }
            .launchIn(lifecycleScope)

        val loginBtn = findViewById<com.google.android.material.button.MaterialButton>(R.id.login_button_login)
        loginBtn.setOnClickListener {
            val login = findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.login_text_input)
                .editText?.text.toString()
            val password = findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.login_password_text_input)
                .editText?.text.toString()
            if(login.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "Все поля должны быть заполнены!", Toast.LENGTH_LONG).show()
            }
            viewModel.login(login, password)
        }
    }
}