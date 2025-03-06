package com.example.myapplication.recipes

import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import android.content.Context

class ApiKeyManager(context: Context) {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "secret_shared_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveApiKey(key: String) {
        sharedPreferences.edit().putString(API_KEY, key).apply()
    }

    fun getApiKey(): String? {
        return sharedPreferences.getString(API_KEY, null)
    }

    companion object {
        private const val API_KEY = "da561bd9f97347c9af7faae934548b79"
    }
}

