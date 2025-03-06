package com.example.myapplication.homePage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.R
import com.example.myapplication.model.Category
import com.example.myapplication.model.Filter
import com.example.myapplication.model.Recipe
import com.example.myapplication.recipes.RecipeApiService
import com.example.myapplication.repository.RecipeRepository
import com.example.recipeapp.adapters.FilterAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers

class HomeFragment : Fragment() {

    private lateinit var viewPagerFeatured: ViewPager2
    private lateinit var featuredAdapter: FeaturedRecipeAdapter
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var filterAdapter: FilterAdapter
    private lateinit var trendingAdapter: TrendingRecipeAdapter
    private lateinit var recipeRepository: RecipeRepository

    // API Key - In a real app, store this securely
    private val apiKey = "YOUR_SPOONACULAR_API_KEY"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize API service and repository
        val apiService = RecipeApiService.create()
        recipeRepository = RecipeRepository(apiService, apiKey)

        setupFeaturedRecipes(view)
        setupCategories(view)
        setupFilters(view)
        setupTrendingRecipes(view)

        // Load data from API
        loadData()
    }

    private fun setupFeaturedRecipes(view: View) {
        viewPagerFeatured = view.findViewById(R.id.viewPagerFeatured)

        featuredAdapter = FeaturedRecipeAdapter { recipe ->
            navigateToRecipeDetail(recipe)
        }

        viewPagerFeatured.adapter = featuredAdapter

        // Connect TabLayout with ViewPager2
        val tabLayout = view.findViewById<com.google.android.material.tabs.TabLayout>(R.id.tabLayoutIndicator)
        TabLayoutMediator(tabLayout, viewPagerFeatured) { _, _ -> }.attach()
    }

    private fun setupCategories(view: View) {
        val rvCategories = view.findViewById<RecyclerView>(R.id.rvCategories)

        val categories = listOf(
            Category("breakfast", "Breakfast", R.drawable.ic_coffee),
            Category("lunch", "Lunch", R.drawable.ic_hamburger),
            Category("dinner", "Dinner", R.drawable.ic_utensils),
            Category("dessert", "Desserts", R.drawable.ic_ice_cream),
            Category("snack", "Quick Meals", R.drawable.ic_bolt),
            Category("healthy", "Healthy", R.drawable.ic_carrot)
        )

        categoryAdapter = CategoryAdapter(categories) { category ->
            navigateToCategoryRecipes(category)
        }

        rvCategories.layoutManager = GridLayoutManager(requireContext(), 3)
        rvCategories.adapter = categoryAdapter
    }

    private fun setupFilters(view: View) {
        val rvFilters = view.findViewById<RecyclerView>(R.id.rvFilters)

        val filters = listOf(
            Filter("under30", "Under 30 mins", "maxReadyTime=30"),
            Filter("vegetarian", "Vegetarian", "diet=vegetarian"),
            Filter("glutenfree", "Gluten-free", "intolerances=gluten")
        )

        filterAdapter = FilterAdapter(filters) { filter ->
            navigateToFilteredRecipes(filter)
        }

        rvFilters.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvFilters.adapter = filterAdapter
    }

    private fun setupTrendingRecipes(view: View) {
        val rvTrending = view.findViewById<RecyclerView>(R.id.rvTrending)

        trendingAdapter = TrendingRecipeAdapter { recipe ->
            navigateToRecipeDetail(recipe)
        }

        rvTrending.layoutManager = LinearLayoutManager(context)
        rvTrending.adapter = trendingAdapter
    }

    private fun loadData() {
        lifecycleScope.launch {
            try {
                // Load featured recipes
                val featuredRecipes = recipeRepository.getFeaturedRecipes(5)
                featuredAdapter.updateData(featuredRecipes)

                // Load trending recipes
                val trendingRecipes = recipeRepository.getTrendingRecipes(10)
                withContext(Dispatchers.Main) {
                    trendingAdapter.updateData(trendingRecipes)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Error loading recipes: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun navigateToRecipeDetail(recipe: Recipe) {
        // Navigate to recipe detail screen
        Toast.makeText(context, "Clicked on: ${recipe.title}", Toast.LENGTH_SHORT).show()
    }

    private fun navigateToCategoryRecipes(category: Category) {
        Toast.makeText(context, "Clicked on category: ${category.name}", Toast.LENGTH_SHORT).show()

        lifecycleScope.launch {
            try {
                val recipes = recipeRepository.getRecipesByCategory(category.id)
                // Navigate to a screen showing these recipes
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Error loading category recipes: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun navigateToFilteredRecipes(filter: Filter) {
        Toast.makeText(context, "Clicked on filter: ${filter.name}", Toast.LENGTH_SHORT).show()

        lifecycleScope.launch {
            try {
                val recipes = recipeRepository.getRecipesByFilter(filter.id)
                // Navigate to a screen showing these recipes
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Error loading filtered recipes: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
