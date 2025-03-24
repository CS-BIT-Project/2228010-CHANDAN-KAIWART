package com.example.myapplication.ui.detail

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
class RecipeDetailViewModel @Inject constructor(
    private val repository: RecipeRepository
) : ViewModel() {

    private val _recipeDetails = MutableLiveData<Resource<Recipe>>()
    val recipeDetails: LiveData<Resource<Recipe>> = _recipeDetails

    fun getRecipeDetails(recipeId: Int) {
        viewModelScope.launch {
            _recipeDetails.value = Resource.Loading()
            val result = repository.getRecipeDetails(recipeId)
            _recipeDetails.value = result
        }
    }
}