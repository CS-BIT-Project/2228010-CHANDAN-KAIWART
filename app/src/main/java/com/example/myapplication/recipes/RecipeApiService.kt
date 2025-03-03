package com.example.myapplication.recipes

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RecipeApiService {
    // Fetch recipes by ingredients
    @GET("recipes/findByIngredients")
    suspend fun getRecipes(
        @Query("ingredients") ingredients: String,
        @Query("number") number: Int = 10,
        @Query("apiKey") apiKey: String
    ): Response<List<Recipe>> // ✅ Response wrapper add

    // Fetch random recipes
    @GET("recipes/random")
    suspend fun getRandomRecipes(
        @Query("number") number: Int = 10,
        @Query("apiKey") apiKey: String
    ): Response<RandomRecipeResponse> // ✅ Response wrapper add
}

