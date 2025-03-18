package com.example.myapplication.recipes

import com.google.gson.annotations.SerializedName

data class Recipe(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("image") val image: String?,
    @SerializedName("readyInMinutes") val readyInMinutes: Int?,
    @SerializedName("servings") val servings: Int?,
    @SerializedName("sourceUrl") val sourceUrl: String?,
    @SerializedName("summary") val summary: String?,
    @SerializedName("instructions") val instructions: String? = null,
    @SerializedName("dishTypes") val dishTypes: List<String>? = null,
    @SerializedName("diets") val diets: List<String>? = null,
    @SerializedName("cuisines") val cuisines: List<String>? = null,
    @SerializedName("aggregateLikes") val aggregateLikes: Int = 0,
    @SerializedName("spoonacularScore") val spoonacularScore: Double = 0.0,
    @SerializedName("healthScore") val healthScore: Double = 0.0,
    @SerializedName("pricePerServing") val pricePerServing: Double = 0.0,
    @SerializedName("cheap") val cheap: Boolean = false,
    @SerializedName("veryHealthy") val veryHealthy: Boolean = false,
    @SerializedName("veryPopular") val veryPopular: Boolean = false,
    @SerializedName("glutenFree") val glutenFree: Boolean = false,
    @SerializedName("vegan") val vegan: Boolean = false,
    @SerializedName("vegetarian") val vegetarian: Boolean = false,
    @SerializedName("dairyFree") val dairyFree: Boolean = false,
    @SerializedName("sustainable") val sustainable: Boolean = false,
    @SerializedName("videoUrl") val videoUrl: String? = null, // Added from the first version
)
