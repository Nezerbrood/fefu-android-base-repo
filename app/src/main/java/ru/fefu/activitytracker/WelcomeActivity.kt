package ru.fefu.activitytracker
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.welcome)
        val registration : Button = findViewById(R.id.welcome_button_registration)
        registration.setOnClickListener {
            val intent = Intent(this@WelcomeActivity, RegistrationActivity::class.java)
            startActivity(intent)
        }
        val  login : Button = findViewById(R.id.welcome_login_button)
        login.setOnClickListener {
            val intent = Intent(this@WelcomeActivity, LoginActivity::class.java)
            startActivity(intent)
        }

    }
}