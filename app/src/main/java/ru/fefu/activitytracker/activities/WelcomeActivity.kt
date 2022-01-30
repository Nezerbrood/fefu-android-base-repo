package ru.fefu.activitytracker.activities
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import ru.fefu.activitytracker.App
import ru.fefu.activitytracker.R

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        if (App.INSTANCE.sharedPrefs.getString("token", null) !== null) {
            val intent = Intent(this@WelcomeActivity, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
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