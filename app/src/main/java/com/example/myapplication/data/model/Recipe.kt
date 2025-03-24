package com.example.myapplication.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Recipe(
    val ingredients: List<String>?, // List of ingredients (nullable)
    val id: Int, // Unique ID for the recipe
    val title: String, // Title of the recipe
    val image: String, // URL or path to the recipe image
    val readyInMinutes: Int, // Time required to prepare the recipe
    val servings: Int, // Number of servings
    val sourceUrl: String, // URL to the original recipe source
    val summary: String, // Short summary of the recipe
    val instructions: String? = null, // Cooking instructions (nullable)
    val videoUrl: String? = null, // URL to the recipe video (nullable)
    val extendedIngredients: List<Ingredient> = emptyList() // List of extended ingredients
) : Parcelable

@Parcelize
data class Ingredient(
    val id: Int, // Unique ID for the ingredient
    val name: String, // Name of the ingredient
    val amount: Double, // Amount of the ingredient
    val unit: String // Unit of measurement (e.g., grams, cups)
) : Parcelable