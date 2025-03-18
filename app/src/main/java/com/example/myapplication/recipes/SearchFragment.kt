package com.example.myapplication.recipes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var recipeAdapter: RecipeAdapter
    private lateinit var searchInput: EditText
    private lateinit var searchButton: Button

    companion object {
        private const val API_KEY = "da561bd9f97347c9af7faae934548b79"
        private const val TAG = "SearchFragment"
        private const val numResults = 40
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.search_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recipesGrid)
        searchInput = view.findViewById(R.id.searchInput)
        searchButton = view.findViewById(R.id.searchButton)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recipeAdapter = RecipeAdapter(mutableListOf())
        recyclerView.adapter = recipeAdapter

        fetchRandomRecipes()

        searchButton.setOnClickListener {
            val query = searchInput.text.toString().trim()
            if (query.isNotEmpty()) {
                fetchRecipesByIngredients(query)
            } else {
                showToast("Please enter ingredients")
            }
        }
    }

    private fun fetchRandomRecipes() {
        lifecycleScope.launch(Dispatchers.IO) { // Run on IO thread for performance
            try {
                // Correct order: pehle numResults, phir API_KEY
                val response = RetrofitInstance.api.getRandomRecipes(numResults, API_KEY)

                if (response.isSuccessful) {
                    val recipesList = response.body()?.recipes // Response body se recipes list lein

                    withContext(Dispatchers.Main) {
                        if (!recipesList.isNullOrEmpty()) {
                            recipeAdapter.updateData(recipesList)
                        } else {
                            showToast("No recipes found")
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        showToast("Error: ${response.message()}")
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching random recipes", e)
                withContext(Dispatchers.Main) {
                    showToast("Error: ${e.localizedMessage ?: "Unknown error"}")
                }
            }
        }
    }

    private fun fetchRecipesByIngredients(ingredients: String) {
        val cleanedIngredients = ingredients.trim()
        if (cleanedIngredients.isEmpty()) {
            showToast("Please enter valid ingredients")
            return
        }

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.api.getRecipes(cleanedIngredients, numResults, API_KEY)

                if (response.isSuccessful) {
                    val recipesList = response.body() ?: emptyList() // Agar response list hai toh direct body() lein

                    withContext(Dispatchers.Main) {
                        if (!recipesList.isNullOrEmpty()) {
                            recipeAdapter.updateData(recipesList)
                        } else {
                            showToast("No recipes found")
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        showToast("Error: ${response.message()}")
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching recipes by ingredients", e)
                withContext(Dispatchers.Main) {
                    showToast("Error: ${e.message}")
                }
            }
        }
    }




    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
