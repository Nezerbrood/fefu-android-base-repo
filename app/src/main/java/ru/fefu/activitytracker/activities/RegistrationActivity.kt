package ru.fefu.activitytracker.activities
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.text.Spanned
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import android.widget.Toast
import ru.fefu.activitytracker.R


class RegistrationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registration)
        val backButton : Button = findViewById(R.id.registration_button_back)
        backButton.setOnClickListener {
            finish()
        }
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
        val text = SpannableString("Нажимая на кнопку, вы соглашаетесь с политикой конфиденциальности и обработки персональных данных, а также принимаете пользовательское соглашение ")
        text.setSpan(MyClickableSpan(), 37, 66, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        text.setSpan(MyClickableSpan(), 118, 145, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        val confirmText : TextView = findViewById(R.id.registration_terms_and_privacy)
        confirmText.text = text
        confirmText.movementMethod = LinkMovementMethod.getInstance()
    }
}