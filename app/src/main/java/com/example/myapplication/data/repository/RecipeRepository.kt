package com.example.myapplication.data.repository

import com.example.myapplication.data.model.Recipe
import com.example.myapplication.data.model.VideoResponse
import com.example.myapplication.util.Resource

interface RecipeRepository {
    suspend fun getRandomRecipes(number: Int ): Resource<List<Recipe>>
    suspend fun searchRecipes(query: String): Resource<List<Recipe>>
    suspend fun getRecipeDetails(id: Int): Resource<Recipe>
    suspend fun getRecipeVideos(query: String): Resource<VideoResponse>
}