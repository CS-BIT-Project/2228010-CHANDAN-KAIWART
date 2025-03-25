package com.example.myapplication

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.loginActivity.AuthActivity
import com.example.myapplication.loginActivity.LoginFragment
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        val progressBar = findViewById<ProgressBar>(R.id.progress_bar)
        val imageView = findViewById<ImageView>(R.id.splash_load_img)
        lateinit var auth: FirebaseAuth
        // Animate progress bar from 0 to 100
        val progressAnimator = ObjectAnimator.ofInt(progressBar, "progress", 0, 100).apply {
            duration = 3000 // Match with delay
            interpolator = LinearInterpolator()
        }
        progressAnimator.start()

        // Rotate the image infinitely during the progress
        val rotateAnimator = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 360f).apply {
            duration = 3000 // 1 second per full rotation
            interpolator = LinearInterpolator()
            repeatCount = ObjectAnimator.INFINITE
        }
        rotateAnimator.start()

        // Navigate to LoginActivity after progress bar completes
        auth = FirebaseAuth.getInstance()

        Handler(Looper.getMainLooper()).postDelayed({

            val currentUser = auth.currentUser

            if (currentUser != null) {
                // User is already logged in, go to MainActivity
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                // User is not logged in, go to AuthActivity which hosts Login/Signup fragments
                startActivity(Intent(this, AuthActivity::class.java))
            }

            finish()
        }, 2000) // 2 seconds delay
    }
}
