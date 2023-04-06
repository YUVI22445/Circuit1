package com.example.circuit
import android.widget.Button
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.circuit.ui.auth.AuthActivity

class LogoPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logo_page)
        val loginRegisterButton = findViewById<Button>(R.id.login_register_button)
        loginRegisterButton.setOnClickListener {
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }

    }

}