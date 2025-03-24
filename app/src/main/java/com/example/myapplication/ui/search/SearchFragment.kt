package com.example.myapplication.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.model.Recipe
import com.example.myapplication.ui.adapters.RecipeAdapter
import com.example.myapplication.util.Resource
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private val viewModel: SearchViewModel by viewModels()
    private lateinit var recipeAdapter: RecipeAdapter

    private lateinit var searchInput: TextInputEditText
    private lateinit var searchButton: Button
    private lateinit var searchResultsList: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var noResultsText: TextView
    private lateinit var errorText: TextView

    private var searchJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        searchInput = view.findViewById(R.id.searchInput)
        searchButton = view.findViewById(R.id.searchButton)
        searchResultsList = view.findViewById(R.id.searchResultsList)
        progressBar = view.findViewById(R.id.progressBar)
        noResultsText = view.findViewById(R.id.noResultsText)
        errorText = view.findViewById(R.id.errorText)

        setupRecyclerView()
        setupSearchListener()
        setupObservers()

        viewModel.fetchRandomRecipes(50)
    }

    private fun setupRecyclerView() {
        recipeAdapter = RecipeAdapter { recipe ->
            navigateToRecipeDetail(recipe) // Pass the entire Recipe object
        }

        searchResultsList.apply {
            adapter = recipeAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupSearchListener() {
        searchInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch()
                return@setOnEditorActionListener true
            }
            false
        }

        searchButton.setOnClickListener {
            performSearch()
        }
    }

    private fun performSearch() {
        val query = searchInput.text.toString().trim()

        if (query.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter a search query", Toast.LENGTH_SHORT).show()
            return
        }

        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            delay(100) // Debounce API calls
            viewModel.searchRecipes(query)
        }
    }

    private fun setupObservers() {
        viewModel.searchResults.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    updateUI(
                        isLoading = false,
                        recipes = resource.data.orEmpty(),
                        errorMessage = null
                    )
                }
                is Resource.Error -> {
                    updateUI(
                        isLoading = false,
                        recipes = emptyList(),
                        errorMessage = resource.message ?: "An unknown error occurred"
                    )
                }
                is Resource.Loading -> {
                    updateUI(isLoading = true)
                }
            }
        }
    }

    private fun updateUI(
        isLoading: Boolean,
        recipes: List<Recipe> = emptyList(),
        errorMessage: String? = null
    ) {
        progressBar.isVisible = isLoading
        searchResultsList.isVisible = !isLoading && recipes.isNotEmpty()
        noResultsText.isVisible = !isLoading && recipes.isEmpty()
        errorText.isVisible = !isLoading && errorMessage != null

        if (errorMessage != null) {
            errorText.text = errorMessage
        }

        // Ensure RecipeAdapter has a submitList method
        recipeAdapter.submitList(recipes)
    }

    private fun navigateToRecipeDetail(recipe: Recipe) {
        // Ensure recipeId is retrieved from the recipe object
        val recipeId = recipe.id

        // Use the correct action and pass the required arguments
        val action = SearchFragmentDirections.actionSearchFragmentToVideoPlayerFragment(
            recipeId = recipeId, // Pass recipeId
            videoUrl = recipe.videoUrl ?: "" // Provide a default value for videoUrl
        )

        // Navigate to the destination
        findNavController().navigate(action)
    }
}
