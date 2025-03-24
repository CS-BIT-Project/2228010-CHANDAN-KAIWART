package com.example.myapplication.data.repository

import com.example.myapplication.data.api.SpoonacularApiService
import com.example.myapplication.data.model.Recipe
import com.example.myapplication.data.model.VideoResponse
import com.example.myapplication.util.Resource
import javax.inject.Inject

class RecipeRepositoryImpl @Inject constructor(
    private val apiService: SpoonacularApiService
) : RecipeRepository {

    override suspend fun getRandomRecipes(number: Int): Resource<List<Recipe>> {
        return try {
            val response = apiService.getRandomRecipes(number = number)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()?.get("recipes") ?: emptyList())
            } else {
                Resource.Error("Failed to load random recipes")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun searchRecipes(query: String): Resource<List<Recipe>> {
        return try {
            val response = apiService.searchRecipes(query)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()?.results ?: emptyList())
            } else {
                Resource.Error("No recipes found for '$query'")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun getRecipeDetails(id: Int): Resource<Recipe> {
        return try {
            val response = apiService.getRecipeDetails(id)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error("Failed to load recipe details")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun getRecipeVideos(query: String): Resource<VideoResponse> {
        return try {
            val response = apiService.getRecipeVideos(query)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error("Failed to load recipe videos")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }
}