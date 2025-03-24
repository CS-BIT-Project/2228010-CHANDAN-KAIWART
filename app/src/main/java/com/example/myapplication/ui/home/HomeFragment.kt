package com.example.myapplication.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.ui.adapters.RecipeAdapter
import com.example.myapplication.util.Resource
import com.example.myapplication.util.hide
import com.example.myapplication.util.loadImage
import com.example.myapplication.util.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var recipeAdapter: RecipeAdapter

    private lateinit var progressBar: ProgressBar
    private lateinit var errorText: TextView
    private lateinit var popularRecipesList: RecyclerView
    private lateinit var todayRecipeCard: CardView
    private lateinit var todayRecipeImage: ImageView
    private lateinit var todayRecipeTitle: TextView
    private lateinit var todayRecipeInfo: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        progressBar = view.findViewById(R.id.progressBar)
        errorText = view.findViewById(R.id.errorText)
        popularRecipesList = view.findViewById(R.id.popularRecipesList)
        todayRecipeCard = view.findViewById(R.id.todayRecipeCard)
        todayRecipeImage = view.findViewById(R.id.todayRecipeImage)
        todayRecipeTitle = view.findViewById(R.id.todayRecipeTitle)
        todayRecipeInfo = view.findViewById(R.id.todayRecipeInfo)

        setupRecyclerView()
        setupObservers()
        setupTodayRecipeCard()
    }

    private fun setupRecyclerView() {
        recipeAdapter = RecipeAdapter { recipe ->
            val action = HomeFragmentDirections.actionHomeFragmentToVideoPlayerFragment(recipe.id, recipe.videoUrl ?: "")
            findNavController().navigate(action)
        }

        popularRecipesList.apply {
            adapter = recipeAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }



    private fun setupTodayRecipeCard() {
        todayRecipeCard.setOnClickListener {
            viewModel.todayRecipe.value?.data?.let { recipe ->
                navigateToVideoPlayer(recipe.id)
            } ?: run {
                Toast.makeText(requireContext(), "No recipe available", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupObservers() {
        viewModel.randomRecipes.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    progressBar.hide()
                    errorText.hide()
                    resource.data?.let { recipes ->
                        recipeAdapter.submitList(recipes)
                    }
                }
                is Resource.Error -> {
                    progressBar.hide()
                    errorText.show()
                    errorText.text = resource.message ?: "An unknown error occurred"
                }
                is Resource.Loading -> {
                    progressBar.show()
                    errorText.hide()
                }
            }
        }

        viewModel.todayRecipe.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    resource.data?.let { recipe ->
                        todayRecipeImage.loadImage(recipe.image)
                        todayRecipeTitle.text = recipe.title
                        todayRecipeInfo.text = "${recipe.readyInMinutes} min | ${recipe.servings} servings"
                    }
                }
                is Resource.Error -> {
                    errorText.show()
                    errorText.text = resource.message ?: "Failed to load today's recipe"
                }
                is Resource.Loading -> {
                    todayRecipeTitle.text = "Loading..."
                }
            }
        }
    }

    private fun navigateToVideoPlayer(recipeId: Int?) {
        if (recipeId == null || recipeId <= 0) {
            Toast.makeText(requireContext(), "Invalid Recipe ID", Toast.LENGTH_SHORT).show()
            return
        }
        val recipe = viewModel.randomRecipes.value?.data?.find { it.id == recipeId }
        val action = HomeFragmentDirections.actionHomeFragmentToVideoPlayerFragment(recipeId, recipe?.videoUrl ?: "")
        findNavController().navigate(action)
    }
}
