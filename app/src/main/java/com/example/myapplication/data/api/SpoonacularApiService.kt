package com.example.myapplication.data.api

import com.example.myapplication.BuildConfig
import com.example.myapplication.data.model.Recipe
import com.example.myapplication.data.model.SearchResponse
import com.example.myapplication.data.model.VideoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SpoonacularApiService {
    companion object {
        const val BASE_URL = "https://api.spoonacular.com/" // Define BASE_URL here
    }

    @GET("food/videos/search")
    suspend fun getRecipeVideos(
        @Query("query") query: String,
        @Query("apiKey") apiKey: String = BuildConfig.SPOONACULAR_API_KEY
    ): Response<VideoResponse>

    @GET("recipes/random")
    suspend fun getRandomRecipes(
        @Query("number") number: Int = 50,
        @Query("apiKey") apiKey: String = BuildConfig.SPOONACULAR_API_KEY
    ): Response<Map<String, List<Recipe>>>

    @GET("recipes/complexSearch")
    suspend fun searchRecipes(
        @Query("query") query: String,
        @Query("number") number: Int = 50,
        @Query("addRecipeInformation") addRecipeInformation: Boolean = true,
        @Query("apiKey") apiKey: String = BuildConfig.SPOONACULAR_API_KEY
    ): Response<SearchResponse>

    @GET("recipes/{id}/information")
    suspend fun getRecipeDetails(
        @Path("id") id: Int,
        @Query("apiKey") apiKey: String = BuildConfig.SPOONACULAR_API_KEY
    ): Response<Recipe>
}