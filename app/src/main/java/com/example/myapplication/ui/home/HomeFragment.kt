package com.example.myapplication.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.api.RetrofitClient
import com.example.myapplication.data.model.Recipe
import com.example.myapplication.ui.adapters.RecipeAdapter
import com.example.myapplication.ui.video.VideoPlayerFragment
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecipeAdapter
    private val recipes = mutableListOf<Recipe>()

    private val spoonApi = RetrofitClient.spoonacularApi
    private val youtubeApi = RetrofitClient.youtubeApi
    private val spoonKey = "6b4d3d230a97483b9957d211992331b4"
    private val youtubeKey = "AIzaSyC6RzxUxRo5I190eSpotjN5DVlQTSN3q1k"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.homeRecipeRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = RecipeAdapter { recipe -> searchVideoAndNavigate(recipe.title) }
        recyclerView.adapter = adapter

        fetchRandomRecipes()
    }

    private fun fetchRandomRecipes() {
        lifecycleScope.launch {
            try {
                val response = spoonApi.getRandomRecipes(10, spoonKey)
                recipes.clear()
                recipes.addAll(response.recipes)
                adapter.submitList(recipes.toList())
            } catch (e: Exception) {
                Log.e("HomeFragment", "Failed to fetch recipes: ${e.message}")
                Toast.makeText(requireContext(), "Failed to load home recipes", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun searchVideoAndNavigate(title: String) {
        lifecycleScope.launch {
            try {
                val response = youtubeApi.searchVideos(
                    query = "$title recipe",
                    apiKey = youtubeKey
                )

                val videoId = response.items.firstOrNull()?.id?.videoId

                if (!videoId.isNullOrEmpty()) {
                    val fragment = VideoPlayerFragment.newInstance(videoId)
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, fragment)
                        .addToBackStack(null)
                        .commit()
                } else {
                    Toast.makeText(requireContext(), "No video found for $title", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("HomeFragmen", "YouTube API Error: ${e.message}", e)
                Toast.makeText(requireContext(), "Failed to load video: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
