package com.example.myapplication.recipes

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RecipeApiService {
    @GET("recipes/findByIngredients")
    fun getRecipes(
        @Query("ingredients") ingredients: String,
        @Query("number") number: Int = 10,
        @Query("apiKey") apiKey: String = "da561bd9f97347c9af7faae934548b79"
    ): Call<List<Recipe>>
}
