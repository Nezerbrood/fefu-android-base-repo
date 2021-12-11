package ru.fefu.activitytracker.activities
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import ru.fefu.activitytracker.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
        val backButton : Button = findViewById(R.id.login_button_back)
        backButton.setOnClickListener {
            finish()
        }
    }

}