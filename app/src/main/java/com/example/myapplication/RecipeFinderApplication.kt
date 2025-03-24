package com.example.myapplication

import android.app.Application
import android.util.Log
import android.widget.Toast
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RecipeFinderApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        validateApiKey()
    }

    private fun validateApiKey() {
        val apiKey = BuildConfig.SPOONACULAR_API_KEY
        Log.d("API_DEBUG", "API Key: $apiKey")
        if (apiKey.isNullOrEmpty()) {
            Toast.makeText(
                this,
                "Spoonacular API key is missing. Please set it in the environment variables.",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}