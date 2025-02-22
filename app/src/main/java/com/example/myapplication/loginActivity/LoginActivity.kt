package com.example.myapplication.loginActivity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var btnLogin: Button
    private lateinit var btnSignUp: Button
    private lateinit var formContainer: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check if user is already logged in
        if (FirebaseAuth.getInstance().currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }
        setContentView(R.layout.activity_login)

        btnLogin = findViewById(R.id.btnLogin)
        btnSignUp = findViewById(R.id.btnSignUp)
        formContainer = findViewById(R.id.formContainer)

        showLoginForm()

        btnLogin.setOnClickListener { showLoginForm() }
        btnSignUp.setOnClickListener { showSignUpForm() }
    }

    private fun showLoginForm() {
        val loginForm = LayoutInflater.from(this).inflate(R.layout.form_login, formContainer, false)
        formContainer.removeAllViews()
        formContainer.addView(loginForm)

        val etEmail = loginForm.findViewById<EditText>(R.id.etEmail)
        val etPassword = loginForm.findViewById<EditText>(R.id.etPassword)
        val btnSubmit = loginForm.findViewById<Button>(R.id.btnSubmit)

        btnSubmit.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, MainActivity::class.java))
                            finish() // Close LoginActivity
                        } else {
                            Toast.makeText(this, "Login Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun showSignUpForm() {
        val signUpForm = LayoutInflater.from(this).inflate(R.layout.form_signup, formContainer, false)
        formContainer.removeAllViews()
        formContainer.addView(signUpForm)

        val etEmail = signUpForm.findViewById<EditText>(R.id.etEmail)
        val etPassword = signUpForm.findViewById<EditText>(R.id.etPassword)
        val btnSubmit = signUpForm.findViewById<Button>(R.id.btnSubmit)

        btnSubmit.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isNotEmpty() && password.length >= 6) {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Sign Up Successful", Toast.LENGTH_SHORT).show()
                            showLoginForm() // Redirect to login form after successful registration
                        } else {
                            Toast.makeText(this, "Sign Up Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Please enter a valid email and password (6+ characters)", Toast.LENGTH_SHORT).show()
            }
        }
    }

}

