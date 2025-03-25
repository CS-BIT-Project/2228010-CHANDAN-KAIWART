package com.example.myapplication.settingPage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.myapplication.databinding.ActivitySettingBinding
import com.example.myapplication.loginActivity.LoginFragment
import com.example.myapplication.util.FontSizeManager
import com.google.firebase.auth.FirebaseAuth

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding
    private val sharedPref by lazy { getSharedPreferences("app_preferences", Context.MODE_PRIVATE) }
    private val fontSizeManager by lazy { FontSizeManager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupDarkModeSwitch()
        setupFontSizeControls()
        setupProfileButton()
        setupLogoutButton()
        setupAboutButton()

        // Apply the current font size to all text views
        applyCurrentFontSize()
    }

    private fun applyCurrentFontSize() {
        // Apply font size to all text views in the activity
        fontSizeManager.applyFontSize(
            binding.themeDisplayTitle,
            binding.darkModeLabel,
            binding.fontSizeLabel,
            binding.fontSizeValueText,
            binding.accountTitle,
            binding.aboutTitle,
            binding.versionTextView
        )
    }

    private fun setupToolbar() {
        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun setupDarkModeSwitch() {
        // Set initial state
        val isDarkMode = sharedPref.getBoolean("dark_mode", false)
        binding.darkModeSwitch.isChecked = isDarkMode

        // Set up listener
        binding.darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            updateDarkModePreference(isChecked)
        }
    }

    private fun setupFontSizeControls() {
        // Get current font size
        val currentSize = fontSizeManager.getCurrentFontSize()

        // Normalize the size between 0-100 for the slider
        // Assuming min size is 12 and max size is 20 (range of 8)
        val normalizedSize = ((currentSize - FontSizeManager.MIN_FONT_SIZE) /
                (FontSizeManager.MAX_FONT_SIZE - FontSizeManager.MIN_FONT_SIZE)) * 100

        // Setup initial slider position
        binding.fontSizeSlider.value = normalizedSize

        // Update size label
        binding.fontSizeValueText.text = currentSize.toInt().toString()

        // Set up listener for slider changes
        binding.fontSizeSlider.addOnChangeListener { _, value, fromUser ->
            if (fromUser) {
                // Convert slider value (0-100) back to font size (12-20)
                val newSize = FontSizeManager.MIN_FONT_SIZE +
                        (value / 100f) * (FontSizeManager.MAX_FONT_SIZE - FontSizeManager.MIN_FONT_SIZE)

                // Update the size display text
                binding.fontSizeValueText.text = newSize.toInt().toString()

                // Setup the slider formatter for when user is dragging
                binding.fontSizeSlider.setLabelFormatter { value ->
                    val size = FontSizeManager.MIN_FONT_SIZE +
                            (value / 100f) * (FontSizeManager.MAX_FONT_SIZE - FontSizeManager.MIN_FONT_SIZE)
                    "${size.toInt()}sp"
                }
            }
        }

        binding.fontSizeApplyButton.setOnClickListener {
            val value = binding.fontSizeSlider.value
            val newSize = FontSizeManager.MIN_FONT_SIZE +
                    (value / 100f) * (FontSizeManager.MAX_FONT_SIZE - FontSizeManager.MIN_FONT_SIZE)

            updateFontSize(newSize)
        }
    }

    private fun setupProfileButton() {
        binding.profileSettingsButton.setOnClickListener {
            // Navigate to profile settings screen
            Toast.makeText(this, "Profile settings coming soon", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupLogoutButton() {
        binding.logoutButton.setOnClickListener {
            confirmLogout()
        }
    }

    private fun setupAboutButton() {
        binding.aboutButton.setOnClickListener {
            showAboutDialog()
        }
    }

    private fun updateDarkModePreference(isEnabled: Boolean) {
        with(sharedPref.edit()) {
            putBoolean("dark_mode", isEnabled)
            apply()
        }

        // Apply theme immediately
        if (isEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        // Restart activity to apply theme
        recreate()
    }

    private fun updateFontSize(size: Float) {
        // Save the new font size
        fontSizeManager.setFontSize(size)

        // Apply it immediately to this activity's views
        applyCurrentFontSize()

        // Show dialog about app restart for complete application
        AlertDialog.Builder(this)
            .setTitle("Font Size Changed")
            .setMessage("The font size has been updated. Do you want to restart the app to apply changes to all screens?")
            .setPositiveButton("Restart Now") { _, _ -> restartApp() }
            .setNegativeButton("Later") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun restartApp() {
        val intent = Intent(this, LoginFragment::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()

        // Add a slight delay and then restart the process
        android.os.Handler(mainLooper).postDelayed({
            val restartIntent = packageManager.getLaunchIntentForPackage(packageName)
            restartIntent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(restartIntent)
        }, 500)
    }

    private fun confirmLogout() {
        AlertDialog.Builder(this)
            .setTitle("Confirm Logout")
            .setMessage("Are you sure you want to log out?")
            .setPositiveButton("Logout") { _, _ ->
                // Sign out from Firebase
                FirebaseAuth.getInstance().signOut()

                // Navigate to login screen
                val intent = Intent(this, LoginFragment::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showAboutDialog() {
        AlertDialog.Builder(this)
            .setTitle("About Recipe Finder")
            .setMessage("Recipe Finder v2.1.0\n\nA comprehensive recipe app that helps you discover delicious recipes based on ingredients you have on hand. Features include search by ingredients, cooking videos, meal planning, and more.\n\n© 2024 Your Company")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}