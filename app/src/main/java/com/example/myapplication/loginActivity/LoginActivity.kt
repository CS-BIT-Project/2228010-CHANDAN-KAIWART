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
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var btnLogin: Button
    private lateinit var btnSignUp: Button
    private lateinit var formContainer: FrameLayout
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Check if user is already logged in
        if (firebaseAuth.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish() // Finish LoginActivity so user can't go back to it
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
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
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
        val etUserName = signUpForm.findViewById<EditText>(R.id.etUserName)
        val btnSubmit = signUpForm.findViewById<Button>(R.id.btnSubmit)

        btnSubmit.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val username = etUserName.text.toString().trim()

            if (email.isNotEmpty() && password.length >= 6 && username.isNotEmpty()) {
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val userId = firebaseAuth.currentUser?.uid ?: return@addOnCompleteListener
                            val userMap = hashMapOf(
                                "userId" to userId,
                                "username" to username,
                                "email" to email,
                                "profileImageUrl" to ""
                            )
                            firestore.collection("users").document(userId).set(userMap)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Sign Up Successful", Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(this, MainActivity::class.java))
                                    finish()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this, "Database Error: ${it.message}", Toast.LENGTH_SHORT).show()
                                }
                        } else {
                            Toast.makeText(this, "Sign Up Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Please enter a valid email, password (6+ characters), and username", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
