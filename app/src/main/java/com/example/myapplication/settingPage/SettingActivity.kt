package com.example.myapplication.settingPage

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivitySettingBinding
import com.example.myapplication.databinding.ItemSettingLogoutBinding
import com.example.myapplication.loginActivity.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding
    private lateinit var adapter: SettingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val logoutBinding = ItemSettingLogoutBinding.inflate(LayoutInflater.from(this))

        logoutBinding.logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        binding.root.addView(logoutBinding.root)

            setupToolbar()
        setupRecyclerView()
    }

    private fun setupToolbar() {
        val backButton: ImageButton = binding.toolbar.findViewById(R.id.backButton)
        backButton.setOnClickListener { finish() }
    }


    private fun setupRecyclerView() {
        adapter = SettingAdapter()
        binding.settingsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@SettingActivity)
            adapter = this@SettingActivity.adapter
            addItemDecoration(SettingSectionDecoration(20))
        }

        // Populate settings items
        adapter.submitList(createSettingsItems())
    }

    private fun createSettingsItems(): List<SettingItem> = buildList {
        // Theme & Display
        add(SettingItem.Header("Theme & Display"))
        add(SettingItem.Switch("Dark Mode", R.drawable.ic_moon, false))
        add(SettingItem.ThemeColors("Theme Colors", listOf("Light", "Dark", "System Default")))
        add(SettingItem.FontSize(16f))

        // Notifications
        add(SettingItem.Header("Notifications"))
        add(SettingItem.Switch("Push Notifications", R.drawable.ic_bell, true))
        add(SettingItem.Switch("Meal Reminders", R.drawable.ic_clock, true))
        add(SettingItem.Switch("New Recipe Alerts", R.drawable.ic_utensils, true))

        // App Permissions
        add(SettingItem.Header("App Permissions"))
        add(SettingItem.Switch("Camera Access", R.drawable.ic_camera, true))
        add(SettingItem.Switch("Photo Library", R.drawable.ic_images, true))
        add(SettingItem.Switch("Location Services", R.drawable.ic_location, false))

        // Security
        add(SettingItem.Header("Security"))
        add(SettingItem.Switch("App Lock", R.drawable.ic_lock, false))
        add(SettingItem.Navigation("Privacy Policy", R.drawable.ic_shield))
        add(SettingItem.Navigation("Terms of Service", R.drawable.ic_file))

        // Profile
        add(SettingItem.Header("Profile"))
        add(SettingItem.Navigation("Account Information", R.drawable.ic_user))
        add(SettingItem.Navigation("Dietary Preferences", R.drawable.ic_leaf))
        add(
            SettingItem.Dropdown(
                "Measurement Units",
                R.drawable.ic_ruler,
                listOf("Metric", "Imperial")
            )
        )

        // Version and Logout
        add(SettingItem.Version("2.1.0"))
        add(SettingItem.Logout)
    }
}
