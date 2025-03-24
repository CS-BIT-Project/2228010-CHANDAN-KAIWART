package com.example.myapplication.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.Recipe
import com.example.myapplication.data.repository.RecipeRepository
import com.example.myapplication.util.Resource
import com.example.myapplication.util.Resource.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: RecipeRepository
) : ViewModel() {

    private val _searchResults = MutableLiveData<Resource<List<Recipe>>>()
    val searchResults: LiveData<Resource<List<Recipe>>> = _searchResults

    fun fetchRandomRecipes(number: Int = 50) { // Add number parameter with default value
        viewModelScope.launch {
            _searchResults.value = Resource.Loading()
            val result = repository.getRandomRecipes(number) // Pass the number parameter
            when (result) {
                is Resource.Success -> {
                    _searchResults.value = Success(result.data ?: emptyList())
                }
                is Resource.Error -> {
                    _searchResults.value = Error(result.message ?: "Unknown error")
                }
                // Resource.Loading is not needed here because we already set it above
                is Resource.Loading<*> -> TODO()
            }
        }
    }

    fun searchRecipes(query: String) {
        viewModelScope.launch {
            _searchResults.value = Resource.Loading()
            val response = repository.searchRecipes(query)
            when (response) {
                is Success -> {
                    response.data?.let { searchResponse ->
                        _searchResults.value = Success(searchResponse)
                    }
                }
                is Error -> {
                    _searchResults.value = Error(response.message ?: "Unknown error")
                }
                // Resource.Loading is not needed here because we already set it above
                is Loading<*> -> TODO()
            }
        }
    }

    fun clearResults() {
        _searchResults.value = Resource.Success(emptyList())
    }
}