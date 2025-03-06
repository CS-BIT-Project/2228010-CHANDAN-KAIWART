package com.example.myapplication.homePage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.model.Recipe  // model ka Recipe class use karna hai // Recipe conversion function import karein

class FeaturedRecipeAdapter(
    private val recipes: MutableList<Recipe> = mutableListOf(),
    private val onItemClick: (Recipe) -> Unit
) : RecyclerView.Adapter<FeaturedRecipeAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val recipeImage: ImageView = view.findViewById(R.id.ivRecipeImage)
        val recipeTitle: TextView = view.findViewById(R.id.tvRecipeTitle)
        val recipeTime: TextView = view.findViewById(R.id.tvRecipeTime)
        val recipeDifficulty: TextView = view.findViewById(R.id.tvRecipeDifficulty)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_featured_recipe, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = recipes[position]

        holder.recipeTitle.text = recipe.title
        holder.recipeTime.text = "${recipe.readyInMinutes} min"

        // Set difficulty based on readyInMinutes
        val difficulty = when {
            recipe.readyInMinutes <= 20 -> "Easy"
            recipe.readyInMinutes <= 40 -> "Medium"
            else -> "Hard"
        }
        holder.recipeDifficulty.text = difficulty

        Glide.with(holder.itemView.context)
            .load(recipe.image)
            .centerCrop()
            .into(holder.recipeImage)

        holder.itemView.setOnClickListener {
            onItemClick(recipe)
        }
    }

    override fun getItemCount() = recipes.size

    fun updateData(newRecipes: List<com.example.myapplication.recipes.Recipe>) {
        // Manually convert API Recipes to Model Recipes
        val convertedRecipes = newRecipes.map { apiRecipe ->
            Recipe(
                id = apiRecipe.id,
                title = apiRecipe.title,
                image = apiRecipe.image ?: "", // null handling
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
