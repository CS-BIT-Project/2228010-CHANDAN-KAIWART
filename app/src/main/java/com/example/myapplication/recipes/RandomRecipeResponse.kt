package com.example.myapplication.recipes

import com.google.gson.annotations.SerializedName

class RandomRecipeResponse {
    @SerializedName("recipes") val recipes: List<Recipe>? = null
}
data class RecipeSearchResponse(
    val results: List<Recipe>,
    val offset: Int,
    val number: Int,
    val totalResults: Int
)

