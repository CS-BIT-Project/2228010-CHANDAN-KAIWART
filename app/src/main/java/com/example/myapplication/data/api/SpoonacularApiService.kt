package com.example.myapplication.data.api

import com.example.myapplication.data.model.*
import retrofit2.http.GET
import retrofit2.http.Query

interface SpoonacularApiService {
    @GET("recipes/findByIngredients")
    suspend fun getRecipesByIngredients(
        @Query("ingredients") ingredients: String,
        @Query("number") number: Int = 10,
        @Query("apiKey") apiKey: String
    ): List<Recipe>

    @GET("recipes/random")
    suspend fun getRandomRecipes(
        @Query("number") number: Int = 10,
        @Query("apiKey") apiKey: String
    ): RandomRecipeResponse


}
data class RandomRecipeResponse(
    val recipes: List<Recipe>
)
