package com.example.myapplication.util

import android.content.Context
import android.content.res.Configuration
import android.util.TypedValue
import android.widget.TextView
import androidx.core.content.ContextCompat

/**
 * Utility class to manage font size preferences across the application
 */
class FontSizeManager(private val context: Context) {

    companion object {
        const val DEFAULT_FONT_SIZE = 16f
        const val MIN_FONT_SIZE = 12f
        const val MAX_FONT_SIZE = 20f
    }

    /**
     * Gets the current font size from shared preferences
     */
    fun getCurrentFontSize(): Float {
        val sharedPref = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        return sharedPref.getFloat("font_size", DEFAULT_FONT_SIZE)
    }

    /**
     * Sets the font size in shared preferences
     */
    fun setFontSize(size: Float) {
        // Ensure size is within valid bounds
        val validSize = size.coerceIn(MIN_FONT_SIZE, MAX_FONT_SIZE)

        // Save to preferences
        val sharedPref = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putFloat("font_size", validSize)
            apply()
        }
    }

    /**
     * Apply the font size to a specific TextView
     */
    fun applyFontSize(textView: TextView) {
        val currentSize = getCurrentFontSize()
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, currentSize)
    }

    /**
     * Apply font size to multiple TextViews at once
     */
    fun applyFontSize(vararg textViews: TextView) {
        val currentSize = getCurrentFontSize()
        textViews.forEach { textView ->
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, currentSize)
        }
    }

    /**
     * Use this method to get a scaled pixel value for the current font size setting
     * This can be used when setting text sizes programmatically
     */
    fun getScaledTextSize(baseTextSize: Float): Float {
        val currentSize = getCurrentFontSize()
        val scaleFactor = currentSize / DEFAULT_FONT_SIZE
        return baseTextSize * scaleFactor
    }

    /**
     * Converts dp value to pixels
     */
    fun dpToPx(dp: Float): Int {
        val metrics = context.resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics).toInt()
    }

    /**
     * Converts sp value to pixels
     */
    fun spToPx(sp: Float): Int {
        val metrics = context.resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, metrics).toInt()
    }
}