package com.example.myapplication.homePage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.model.Recipe
import com.example.myapplication.recipes.Recipe as ApiRecipe // API Recipe class ko alias de rahe hain

class TrendingRecipeAdapter(
    private val recipes: MutableList<Recipe> = mutableListOf(),
    private val onItemClick: (Recipe) -> Unit
) : RecyclerView.Adapter<TrendingRecipeAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val recipeImage: ImageView = view.findViewById(R.id.ivRecipeImage)
        val recipeTitle: TextView = view.findViewById(R.id.tvRecipeTitle)
        val recipeRating: TextView = view.findViewById(R.id.tvRecipeRating)
        val recipeTime: TextView = view.findViewById(R.id.tvRecipeTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_trending_recipe, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = recipes[position]

        holder.recipeTitle.text = recipe.title
        holder.recipeTime.text = "${recipe.readyInMinutes} min"

        // Calculate rating from spoonacularScore (0-100) to (0-5)
        val rating = (recipe.spoonacularScore / 20).toFloat()
        holder.recipeRating.text = String.format("%.1f", rating)

        Glide.with(holder.itemView.context)
            .load(recipe.image)
            .centerCrop()
            .into(holder.recipeImage)

        holder.itemView.setOnClickListener {
            onItemClick(recipe)
        }
    }

    override fun getItemCount() = recipes.size

    // ✅ Fix: Convert API Recipe to Model Recipe
    fun updateData(newRecipes: List<ApiRecipe>) {
        val convertedRecipes = newRecipes.map { apiRecipe ->
            Recipe(
                id = apiRecipe.id,
                title = apiRecipe.title,
                image = apiRecipe.image ?: "", // Default empty string if null
                readyInMinutes = apiRecipe.readyInMinutes,
                servings = 0, // Default value
                sourceUrl = "",
                summary = "",
                instructions = "",
                dishTypes = emptyList(),
                diets = emptyList(),
                cuisines = emptyList(),
                aggregateLikes = 0,
                spoonacularScore = apiRecipe.spoonacularScore?.toDouble() ?: 0.0,
                healthScore = 0.0,
                pricePerServing = 0.0,
                cheap = false,
                veryHealthy = false,
                veryPopular = false,
                glutenFree = false,
                vegan = false,
                vegetarian = false,
                dairyFree = false,
                sustainable = false
            )
        }
        recipes.clear()
        recipes.addAll(convertedRecipes)
        notifyDataSetChanged()
    }
}
