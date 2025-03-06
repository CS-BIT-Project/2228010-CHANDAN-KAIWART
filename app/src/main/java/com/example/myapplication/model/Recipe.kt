package com.example.myapplication.model

data class Recipe(
    val id: Int,
    val title: String,
    val image: String,
    val readyInMinutes: Int,
    val servings: Int,
    val sourceUrl: String,
    val summary: String,
    val instructions: String?,
    val dishTypes: List<String>?,
    val diets: List<String>?,
    val cuisines: List<String>?,
    val aggregateLikes: Int,
    val spoonacularScore: Double,
    val healthScore: Double,
    val pricePerServing: Double,
    val cheap: Boolean,
    val veryHealthy: Boolean,
    val veryPopular: Boolean,
    val glutenFree: Boolean,
    val vegan: Boolean,
    val vegetarian: Boolean,
    val dairyFree: Boolean,
    val sustainable: Boolean
)
