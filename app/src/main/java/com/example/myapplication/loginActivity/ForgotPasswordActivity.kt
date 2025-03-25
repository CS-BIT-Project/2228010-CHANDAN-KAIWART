package com.example.myapplication.loginActivity

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Set up reset button click listener
        binding.buttonReset.setOnClickListener {
            resetPassword()
        }

        // Set up back button with animation
        binding.backButton.setOnClickListener {
            // Use the shared element transition for going back
            finishAfterTransition()
        }
    }

    private fun resetPassword() {
        val email = binding.editTextEmail.text.toString().trim()

        // Validate email
        if (TextUtils.isEmpty(email)) {
            binding.editTextEmail.error = "Email is required"
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.editTextEmail.error = "Please enter a valid email"
            return
        }

        // Show progress indicator
        setLoading(true)

        // Send password reset email
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Show success message
                    binding.textViewMessage.visibility = View.VISIBLE
                    binding.textViewMessage.text = "Password reset link sent to your email"
                    binding.textViewMessage.setTextColor(resources.getColor(R.color.success_green, theme))
                    binding.editTextEmail.text?.clear()

                    // Disable reset button after successful reset to prevent multiple emails
                    binding.buttonReset.isEnabled = false
                    binding.buttonReset.text = "Link Sent"

                    // Create a delayed action to return to login screen
                    binding.buttonReset.postDelayed({
                        finishAfterTransition()
                    }, 3000) // 3 seconds delay
                } else {
                    // Show error message
                    binding.textViewMessage.visibility = View.VISIBLE

                    // Customize error message based on the error
                    val errorMessage = when {
                        task.exception?.message?.contains("no user record") == true ->
                            "No account found with this email address"
                        else -> "Failed to send reset link: ${task.exception?.message}"
                    }

                    binding.textViewMessage.text = errorMessage
                    binding.textViewMessage.setTextColor(resources.getColor(R.color.error_red, theme))
                }

                // Hide progress indicator
                setLoading(false)
            }
    }

    private fun setLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.buttonReset.isEnabled = false
            binding.progressIndicator.visibility = View.VISIBLE
        } else {
            // Only re-enable the button if we haven't successfully sent a reset email
            if (binding.textViewMessage.text != "Password reset link sent to your email") {
                binding.buttonReset.isEnabled = true
            }
            binding.progressIndicator.visibility = View.GONE
        }
    }

    override fun onBackPressed() {
        // Override to use the shared element transition
        finishAfterTransition()
    }
}