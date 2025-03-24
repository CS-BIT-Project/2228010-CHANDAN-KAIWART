package com.example.myapplication.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.Recipe
import com.example.myapplication.data.repository.RecipeRepository
import com.example.myapplication.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: RecipeRepository
) : ViewModel() {

    private val _randomRecipes = MutableLiveData<Resource<List<Recipe>>>()
    val randomRecipes: LiveData<Resource<List<Recipe>>> = _randomRecipes

    private val _todayRecipe = MutableLiveData<Resource<Recipe>>()
    val todayRecipe: LiveData<Resource<Recipe>> = _todayRecipe

    init {
        getRandomRecipes()
    }

    fun getRecipesWithVideos(query: String) {
        viewModelScope.launch {
            _randomRecipes.value = Resource.Loading()

            val recipeResult = repository.searchRecipes(query)
            val videoResult = repository.getRecipeVideos(query) // Call the repository method

            when {
                recipeResult is Resource.Success && videoResult is Resource.Success -> {
                    val recipes = recipeResult.data ?: emptyList()
                    val videos = videoResult.data?.videos ?: emptyList()

                    // Matching recipes with videos
                    val recipesWithVideos = recipes.map { recipe ->
                        val relatedVideo = videos.firstOrNull { video -> video.title.contains(recipe.title, ignoreCase = true) }
                        recipe.copy(videoUrl = relatedVideo?.getVideoUrl() ?: "")
                    }

                    _randomRecipes.postValue(Resource.Success(recipesWithVideos))
                }
                recipeResult is Resource.Error -> {
                    _randomRecipes.postValue(Resource.Error(recipeResult.message ?: "Failed to load recipes"))
                }
                videoResult is Resource.Error -> {
                    _randomRecipes.postValue(Resource.Error(videoResult.message ?: "Failed to load videos"))
                }
                else -> {
                    _randomRecipes.postValue(Resource.Error("Failed to load recipes or videos"))
                }
            }
        }
    }

    fun getRandomRecipes() {
        viewModelScope.launch {
            _randomRecipes.value = Resource.Loading()

            val result = repository.getRandomRecipes(50)
            _randomRecipes.postValue(result)

            if (result is Resource.Success) {
                result.data?.let { recipes ->
                    if (recipes.isNotEmpty()) {
                        _todayRecipe.postValue(Resource.Success(recipes.first()))
                    } else {
                        _todayRecipe.postValue(Resource.Error("No recipes available"))
                    }
                }
            } else if (result is Resource.Error) {
                _todayRecipe.postValue(Resource.Error(result.message ?: "Unknown error"))
            }
        }
    }

    fun refreshData() {
        getRandomRecipes()
    }
}