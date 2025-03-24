package com.example.myapplication.data.api

import com.example.myapplication.data.model.Recipe
import com.example.myapplication.data.model.SearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeApiService {
    @GET("recipes/complexSearch")
    suspend fun searchRecipes(
        @Query("query") query: String,
        @Query("number") number: Int = 50,
        @Query("addRecipeInformation") addRecipeInformation: Boolean = true
    ): Response<SearchResponse>

    @GET("recipes/random")
    suspend fun getRandomRecipes(
        @Query("number") number: Int = 50
    ): Response<Map<String, List<Recipe>>>

    @GET("recipes/{id}/information")
    suspend fun getRecipeDetails(
        @Path("id") id: Int
    ): Response<Recipe>
}