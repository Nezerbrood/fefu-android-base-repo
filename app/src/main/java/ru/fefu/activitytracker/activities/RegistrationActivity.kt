package ru.fefu.activitytracker.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.fefu.activitytracker.App
import ru.fefu.activitytracker.R
import ru.fefu.activitytracker.activities.viewModels.RegistrationViewModel
import ru.fefu.activitytracker.retrofit.Result
import ru.fefu.activitytracker.retrofit.response.TokenUserModel


class RegistrationActivity : AppCompatActivity() {
    private lateinit var viewModel: RegistrationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[RegistrationViewModel::class.java]

        setContentView(R.layout.registration)
        val items = mutableListOf<String>()

        class MyClickableSpan : ClickableSpan() {
            override fun onClick(textView: View) {
                Toast.makeText(this@RegistrationActivity, "test", Toast.LENGTH_SHORT).show()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = Color.parseColor("#4B09F3")
                ds.isUnderlineText = false
            }
        }

        val backButton : MaterialButton = findViewById(R.id.registration_button_back)
        backButton.setOnClickListener {
            val intent = Intent(this@RegistrationActivity, WelcomeActivity::class.java)
            startActivity(intent)
        }

        viewModel.dataFlow
            .onEach {
                if (it is Result.Success<TokenUserModel>) {
                    App.INSTANCE.sharedPrefs.edit().putString("token", it.result.token).apply()
                    val intent = Intent(this@RegistrationActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                } else if (it is Result.Error<TokenUserModel>) {
                    //Toast.makeText(this, it.e.toString(), Toast.LENGTH_LONG).show()
                    var error = it.e
                    if (error is retrofit2.HttpException) {
                        if (error.response()?.code() == 422){
                            Toast.makeText(this,
                                "Такое значение поля «login» уже используется.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }else{

                    }
                }else{
                }
            }
            .launchIn(lifecycleScope)

        val registerBtn = findViewById<com.google.android.material.button.MaterialButton>(R.id.registration_button_registration)
        registerBtn.setOnClickListener {
            val login =
                findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.registration_login_input)
                    .editText?.text.toString()
            val password =
                findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.registration_password_input)
                    .editText?.text.toString()
            val password_repeat =
                findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.registration_repassort_input)
                    .editText?.text.toString()
            val name =
                findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.registration_nickname_input)
                    .editText?.text.toString()
            val registrationRadioGroup = findViewById<RadioGroup>(R.id.registration_radio_group)
            var gender = -1
            when {
                login.length<3 -> {
                    Toast.makeText(this, "Длина логина должна быть от 3 символов", Toast.LENGTH_LONG).show()
                }
                password.length<8 -> {
                    Toast.makeText(this, "Длина пароля должна быть от 8 символов", Toast.LENGTH_LONG).show()
                }
                name.isEmpty() -> {
                    Toast.makeText(this, "Поле имени не может быть пустым", Toast.LENGTH_LONG).show()
                }
                registrationRadioGroup.checkedRadioButtonId == -1 -> Toast.makeText(this, "Укажите пол", Toast.LENGTH_LONG).show()
                password != password_repeat -> {
                    Toast.makeText(this, "Пароли не совпадают!", Toast.LENGTH_LONG).show()
                }
                else -> {
                    var gender = when (R.id.registration_radio_male) {
                        registrationRadioGroup.checkedRadioButtonId -> {
                            0
                        }
                        else -> {
                            1
                        }
                    }
                    viewModel.register(login, password, name, gender)
                }
            }

        }

        val text = SpannableString("Нажимая на кнопку, вы соглашаетесь с политикой конфиденциальности и обработки персональных данных, а также принимаете пользовательское соглашение ")
        text.setSpan(MyClickableSpan(), 37, 66, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        text.setSpan(MyClickableSpan(), 118, 145, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        val confirmText : TextView = findViewById(R.id.registration_terms_and_privacy)
        confirmText.text = text
        confirmText.movementMethod = LinkMovementMethod.getInstance();
    }
}