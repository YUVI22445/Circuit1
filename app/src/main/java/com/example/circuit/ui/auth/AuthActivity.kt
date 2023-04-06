package com.example.circuit.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.circuit.R
import com.example.circuit.data.AuthManager
import com.example.circuit.databinding.ActivityAuthBinding
import com.example.circuit.ui.home.MainActivity

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (AuthManager.isLoggedIn) {
            navigateHome()
        }

        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.emailField.addTextChangedListener {
            binding.errorTv.text = ""
        }

        binding.passwordField.addTextChangedListener {
            binding.errorTv.text = ""
        }


        binding.login.setOnClickListener {
            performAuth(isLogin = true)
        }

        binding.register.setOnClickListener {
            performAuth(isLogin = false)
        }
    }

    private fun performAuth(isLogin: Boolean) {
        val email = binding.emailField.text.toString()
        val password = binding.passwordField.text.toString()

        if (email.isBlank() || password.isBlank()) {
            binding.errorTv.text = getString(R.string.blank_email_or_password)
            return
        }

        (if (isLogin) {
            AuthManager.login(email, password)
        } else {
            AuthManager.register(email, password)
        }).onSuccess {
            navigateHome()
        }.onFailure {
            binding.errorTv.text = it.message
        }
    }

    private fun navigateHome() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}