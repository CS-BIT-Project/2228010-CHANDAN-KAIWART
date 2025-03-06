package com.example.myapplication.repository

import com.example.myapplication.model.Recipe // ✅ Ensure correct Recipe model is used
import com.example.myapplication.recipes.RecipeApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class RecipeRepository(
    private val apiService: RecipeApiService,
    private val apiKey: String
) {
    suspend fun getFeaturedRecipes(count: Int = 5): List<Recipe> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getRandomRecipes(
                number = count.toString(),
                apiKey = apiKey,
                tags = "popular"
            )
            if (response.isSuccessful) {
                response.body()?.recipes ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: IOException) {
            emptyList() // ✅ Handle network failure
        } catch (e: HttpException) {
            emptyList() // ✅ Handle API failure
        } as List<Recipe>
    }

    suspend fun getTrendingRecipes(count: Int = 10): List<Recipe> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.searchRecipes(
                apiKey = apiKey,
                number = count,
                sort = "popularity"
            )
            if (response.isSuccessful) {
                response.body()?.results ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: IOException) {
            emptyList()
        } catch (e: HttpException) {
            emptyList()
        } as List<Recipe>
    }

    suspend fun getRecipesByCategory(category: String, count: Int = 10): List<Recipe> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.searchRecipes(
                apiKey = apiKey,
                type = category,
                number = count
            )
            if (response.isSuccessful) {
                response.body()?.results ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: IOException) {
            emptyList()
        } catch (e: HttpException) {
            emptyList()
        } as List<Recipe>
    }

    suspend fun getRecipesByFilter(filter: String, count: Int = 10): List<Recipe> = withContext(Dispatchers.IO) {
        try {
            val response = when (filter) {
                "under30" -> apiService.searchRecipes(
                    apiKey = apiKey,
                    maxReadyTime = 30,
                    number = count
                )

                "vegetarian" -> apiService.searchRecipes(
                    apiKey = apiKey,
                    diet = "vegetarian",
                    number = count
                )

                "glutenfree" -> apiService.searchRecipes(
                    apiKey = apiKey,
                    intolerances = "gluten",
                    number = count
                )

                else -> apiService.searchRecipes(
                    apiKey = apiKey,
                    number = count
                )
            }
            if (response.isSuccessful) {
                response.body()?.results ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: IOException) {
            emptyList()
        } catch (e: HttpException) {
            emptyList()
        } as List<Recipe>
    }
}
